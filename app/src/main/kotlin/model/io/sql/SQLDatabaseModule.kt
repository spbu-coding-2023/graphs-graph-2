package model.io.sql

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import model.graphs.abstractGraph.Graph
import view.MainScreen
import view.utils.ErrorWindow
import view.utils.getGraphVMParameter
import viewmodel.MainScreenViewModel
import viewmodel.graph.GraphViewModel
import viewmodel.graph.SetupGraphViewModel
import java.io.File
import java.sql.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object SQLDatabaseModule {
    private const val DB_DIRECTORY = "database"
    private const val DB_NAME = "my_graph_database.db"
    private const val QUERY_NAME = "queries.txt"
    private const val QUERY_DIRECTORY = "src/main/kotlin/model/io/sql/"
    private val DB_URL = "jdbc:sqlite:${File(DB_DIRECTORY, DB_NAME).absolutePath}"

    private fun readQueriesFromFile(): String {
        val fileContent = File(QUERY_DIRECTORY, QUERY_NAME).readText()
        return fileContent.trim()  // Trim any leading/trailing whitespace
    }

    private val insertQueries = readQueriesFromFile()

    init {
        createDatabaseDirectory()
        createTables()
    }

    private fun createDatabaseDirectory() {
        val dbDir = File(DB_DIRECTORY)
        if (!dbDir.exists()) {
            dbDir.mkdirs()
        }
    }

    private fun getConnection(): Connection {
        return DriverManager.getConnection(DB_URL)
    }

    private fun createTables() {
        val createTableSQL = insertQueries.split(":")[7].trimIndent()

        val connection = getConnection()
        connection.use { conn ->
            conn.createStatement().use { statement ->
                statement.executeUpdate(createTableSQL)
            }
        }
    }


    fun <D> insertGraph(graphVM: GraphViewModel<D>, graphName: String, graphType: String) {
        val insertGraphSQL = insertQueries.split(":")[0]
        val insertEdgesSQL = insertQueries.split(":")[1]
        val insertVerticesSQL = insertQueries.split(":")[2]

        getConnection().use { connection ->
            connection.autoCommit = false
            try {
                var graphId: Int
                connection.prepareStatement(insertGraphSQL, Statement.RETURN_GENERATED_KEYS).use { statement ->
                    statement.setString(1, graphName)
                    statement.setString(2, graphType.split(" ")[0].replace("Graph", ""))
                    statement.setString(3, graphType.split(" ")[1])
                    statement.executeUpdate()
                    val generatedKeys = statement.generatedKeys
                    if (generatedKeys.next()) {
                        graphId = generatedKeys.getInt(1)
                    } else {
                        throw SQLException("Failed to insert graph, no ID obtained.")
                    }
                }

                graphVM.graph.getEdges().forEach { edge ->
                    connection.prepareStatement(insertEdgesSQL).use { statement ->
                        statement.setInt(1, graphId)
                        statement.setInt(2, edge.vertex1.id)
                        statement.setInt(3, edge.vertex2.id)
                        graphVM.graph.getWeightMap()[edge]?.let { weight ->
                            statement.setInt(4, weight)
                        } ?: statement.setNull(4, Types.INTEGER)
                        statement.executeUpdate()
                    }
                }

                graphVM.graph.getVertices().forEach { vertex ->
                    connection.prepareStatement(insertVerticesSQL).use { statement ->
                        statement.setInt(1, graphId)
                        statement.setString(2, vertex.data.toString())
                        statement.executeUpdate()
                    }
                }

                connection.commit()
            } catch (ex: SQLException) {
                connection.rollback()
                throw ex
            } finally {
                connection.autoCommit = true
            }
        }
    }

    @Composable
    fun getGraphNames(graphNames: MutableState<ArrayList<Pair<Int, String>>>) {
        val selectNamesSQL = insertQueries.split(":")[3]
        val showErrorMessage = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }
<<<<<<< HEAD
        graphNames.value = arrayListOf()
=======
>>>>>>> bae8cff (feat: add error message handle)

        try {
            getConnection().use { connection ->
                connection.prepareStatement(selectNamesSQL).use { statement ->
                    val resultSet = statement.executeQuery()
                    while (resultSet.next()) {
                        graphNames.value.add(Pair(resultSet.getInt("id"), resultSet.getString("name")))
                    }
                }
            }
        } catch (e: SQLException) {
            showErrorMessage.value = true
            errorMessage.value = e.message.toString()
        }
        if (showErrorMessage.value) {
            ErrorWindow(errorMessage.value, { System.exit(-1) })
        }
    }


    @Composable
    fun <D> importGraph(graphId: Int) {
        val graphVMState = remember { mutableStateOf<GraphViewModel<D>?>(null) }
        val showErrorMessage = remember { mutableStateOf(false) }
        val updateIsRequired = remember { mutableStateOf(false) }
        var currentGraphSetup: Pair<Triple<SetupGraphViewModel.GraphType,
                SetupGraphViewModel.GraphStructure, SetupGraphViewModel.Weight>, String>? = null

        try {
            val connection = getConnection()
            connection.use {
                val selectGraphSQL = insertQueries.split(":")[6]
                it.prepareStatement(selectGraphSQL).use { statement ->
                    statement.setInt(1, graphId)
                    val resultSet = statement.executeQuery()

                    if (resultSet.next()) {
                        currentGraphSetup = importGraphInfo(graphId)
                    } else {
                        showErrorMessage.value = true
                        throw SQLException("Graph with ID $graphId not found.")
                    }
                }
            }


            // Execute side-effect to create graph object
            SetupGraphViewModel().createGraphObject(
                currentGraphSetup?.first?.first as SetupGraphViewModel.GraphType,
                currentGraphSetup?.first?.second as SetupGraphViewModel.GraphStructure,
                currentGraphSetup?.first?.third as SetupGraphViewModel.Weight,
                graphId,
                graphVMState
            )
            updateIsRequired.value = true

        } catch (e: SQLException) {
            e.printStackTrace()
            showErrorMessage.value = true
        }

        if (updateIsRequired.value) return importGraphUI(showErrorMessage, graphVMState, graphId)
    }

    @Composable
    fun <D> importGraphUI(
        showErrorMessage: MutableState<Boolean>,
        graphVMState: MutableState<GraphViewModel<D>?>,
        graphId: Int
    ) {
        if (showErrorMessage.value) {
            ErrorWindow("Graph with ID $graphId not found.", { System.exit(-1) })
            System.exit(-1)
        }
        if (graphVMState.value != null) {
            graphVMState.value?.updateIsRequired?.value = true

            MainScreen(
                MainScreenViewModel(
                    graphVMState.value?.graph as Graph<D>,
                    graphVMState.value?.graphType?.value as String,
                    graphVMState.value
                )
            )

        } else CircularProgressIndicator()
    }

    fun <D> updateImportedGraphVM(
        graph: Graph<D>,
        graphId: Int,
        graphVMState: MutableState<GraphViewModel<D>?>
    ): GraphViewModel<D>? {
        val selectGraphSQL = insertQueries.split(":")[6]
        val selectVerticesSQL = insertQueries.split(":")[4]
        val selectEdgesSQL = insertQueries.split(":")[5]
        try {
            getConnection().use { connection ->
                connection.prepareStatement(selectGraphSQL).use { statement ->
                    statement.setInt(1, graphId)
                    val resultSet = statement.executeQuery()
                    if (resultSet.next()) {
                        val currentGraphSetup = importGraphInfo(graphId)
                        val graphVMType =
<<<<<<< HEAD
                            mutableStateOf(currentGraphSetup.first.second.toString() + "Graph" + " " + currentGraphSetup.first.first.toString())
=======
                            mutableStateOf(currentGraphSetup.first.second.toString() + "Graph" + " " + currentGraphSetup.first.toString())
>>>>>>> bae8cff (feat: add error message handle)

                        val graphVM = GraphViewModel(
                            graph,
                            mutableStateOf(false),
                            mutableStateOf(false),
                            graphVMType,
                            mutableStateOf(currentGraphSetup.first.second.toString().contains("Directed"))
                        )

                        // Fetch vertices
                        connection.prepareStatement(selectVerticesSQL).use { vertexStatement ->
                            vertexStatement.setInt(1, graphId)
                            val vertexResultSet = vertexStatement.executeQuery()
                            while (vertexResultSet.next()) {
                                val vertexData = vertexResultSet.getString("data")
                                graphVM.addVertex(vertexData)
                            }
                        }

                        // Fetch edges
                        connection.prepareStatement(selectEdgesSQL).use { edgeStatement ->
                            edgeStatement.setInt(1, graphId)
                            val edgeResultSet = edgeStatement.executeQuery()
                            while (edgeResultSet.next()) {
                                val vertex1Id = edgeResultSet.getInt("vertex1_id")
                                val vertex2Id = edgeResultSet.getInt("vertex2_id")
                                val weight = edgeResultSet.getInt("weight")

                                graphVM.addEdge(vertex1Id, vertex2Id, weight)
                            }
                        }
                        graphVMState.value = graphVM

                    } else {
                        throw SQLException("Graph with ID $graphId not found.")
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return graphVMState.value
    }


    private fun importGraphInfo(graphId: Int): Pair<Triple<SetupGraphViewModel.GraphType, SetupGraphViewModel.GraphStructure, SetupGraphViewModel.Weight>, String> {
        val selectGraphSQL = insertQueries.split(":")[6]
        var graphStructure: Int?
        var graphWeight: Int?
        var storedValueType: Int?
        val graphName: String?
        getConnection().use { connection ->
            connection.prepareStatement(selectGraphSQL).use { statement ->
                statement.setInt(1, graphId)
                val resultSet = statement.executeQuery()
                graphName = resultSet.getString("name")
                val graphType = resultSet.getString("graph_type")

                graphStructure = if (graphType.contains("Undirected")) 0 else 1
                graphWeight = if (graphType.contains("Weighted")) 0 else 1

                storedValueType =
                    if (resultSet.getString("stored_value_type") == "Int") 0
                    else if (resultSet.getString("stored_value_type") == "UInt") 1 else 2
            }
        }
        return Pair(graphWeight?.let {
            storedValueType?.let { it1 ->
                graphStructure?.let { it2 ->
                    getGraphVMParameter(it1, it2, it)
                }
            }
        } ?: throw NoSuchElementException("No info found about graph with ID = ${graphId}"),
            graphName ?: throw NoSuchElementException("Graph with ID = ${graphId} has no name"))
    }

    fun deleteGraph(graphId: Int) {
        val deleteGraphSQL = insertQueries.split(":")[8]
        val deleteGraphEdgesSQL = insertQueries.split(":")[9]
        val deleteGraphVerticesSQL = insertQueries.split(":")[10]
        try {
            getConnection().use { connection ->
                connection.prepareStatement(deleteGraphSQL).use { graphstatement ->
                    graphstatement.setInt(1, graphId)
                    graphstatement.executeUpdate()
                }
                connection.prepareStatement(deleteGraphEdgesSQL).use { edgeStatement ->
                    edgeStatement.setInt(1, graphId)
                    edgeStatement.executeUpdate()
                }
                connection.prepareStatement(deleteGraphVerticesSQL).use { vertexStatement ->
                    vertexStatement.setInt(1, graphId)
                    vertexStatement.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun renameGraph(graphId: Int, newGraphName: String) {
        val renameGraphSQL = insertQueries.split(":")[11]
        try {
            getConnection().use { connection ->
                connection.prepareStatement(renameGraphSQL).use { graphstatement ->
                    graphstatement.setString(1, newGraphName)
                    graphstatement.setInt(2, graphId)
                    graphstatement.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
