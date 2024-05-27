package model.io.sql

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import kotlinx.coroutines.*
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

    fun getGraphNames(): ArrayList<Pair<Int, String>> {
        val selectNamesSQL = insertQueries.split(":")[3]
        val graphNames = arrayListOf<Pair<Int, String>>()

        try {
            getConnection().use { connection ->
                connection.prepareStatement(selectNamesSQL).use { statement ->
                    val resultSet = statement.executeQuery()
                    while (resultSet.next()) {
                        graphNames.add(Pair(resultSet.getInt("id"), resultSet.getString("name")))
                    }
                }
            }
        } catch (e: SQLException) {
            // ErrorWindow(e.message.toString(), { System.exit(0) })
        }

        return graphNames
    }


    @Composable
    fun <D> importGraph(graphId: Int) {
        val graphVMState = remember { mutableStateOf<GraphViewModel<D>?>(null) }
        val showErrorMessage = remember { mutableStateOf(false) }
        val updateIsRequired = remember { mutableStateOf(false) }
        var currentGraphSetup: Triple<SetupGraphViewModel.GraphType,
                SetupGraphViewModel.GraphStructure, SetupGraphViewModel.Weight>? = null

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
                currentGraphSetup!!.first,
                currentGraphSetup!!.second,
                currentGraphSetup!!.third,
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
        if (showErrorMessage.value) ErrorWindow("Graph with ID $graphId not found.", { System.exit(-1) })

        if (graphVMState.value != null) {
            graphVMState.value?.updateIsRequired?.value = true

            return MainScreen(
                MainScreenViewModel(
                    graphVMState.value!!.graph,
                    graphVMState.value!!.graphType.value,
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
                            mutableStateOf(currentGraphSetup.second.toString() + "Graph" + " " + currentGraphSetup.first.toString())

                        val graphVM = GraphViewModel(
                            graph,
                            mutableStateOf(false),
                            mutableStateOf(false),
                            graphVMType,
                            mutableStateOf(currentGraphSetup.second.toString().contains("Directed"))
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

                                graphVM.addEdge(vertex1Id, vertex2Id)
                                // TODO: add weight
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


    private fun importGraphInfo(graphId: Int): Triple<SetupGraphViewModel.GraphType, SetupGraphViewModel.GraphStructure, SetupGraphViewModel.Weight> {
        val selectGraphSQL = insertQueries.split(":")[6]
        var graphStructure: Int?
        var graphWeight: Int?
        var storedValueType: Int?
        getConnection().use { connection ->
            connection.prepareStatement(selectGraphSQL).use { statement ->
                statement.setInt(1, graphId)
                val resultSet = statement.executeQuery()
                // val graphName = resultSet.getString("name") // TODO
                val graphType = resultSet.getString("graph_type")

                graphStructure = if (graphType.contains("Undirected")) 0 else 1
                graphWeight = if (graphType.contains("Weighted")) 0 else 1

                storedValueType =
                    if (resultSet.getString("stored_value_type") == "Int") 0
                    else if (resultSet.getString("stored_value_type") == "UInt") 1 else 2
            }
        }
        return graphWeight?.let {
            storedValueType?.let { it1 ->
                graphStructure?.let { it2 ->
                    getGraphVMParameter(it1, it2, it)
                }
            }
        } ?: throw NoSuchElementException("")
    }
}
