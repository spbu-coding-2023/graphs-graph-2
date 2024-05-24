import model.graphs.abstractGraph.Edge
import viewmodel.graph.GraphViewModel
import java.sql.*

import java.io.File

object SQLDatabaseModule {
    private const val DB_DIRECTORY = "database"
    private const val DB_NAME = "my_graph_database.db"
    private val DB_URL = "jdbc:sqlite:${File(DB_DIRECTORY, DB_NAME).absolutePath}"

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

    fun createTables() {
        val createTableSQL = """
        PRAGMA foreign_keys = ON;

        CREATE TABLE IF NOT EXISTS graphs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            graph_type TEXT NOT NULL,
            stored_value_type TEXT NOT NULL
        );

        CREATE TABLE IF NOT EXISTS edges (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            graph_id INTEGER NOT NULL,
            vertex1_id INTEGER NOT NULL,
            vertex2_id INTEGER NOT NULL,
            weight INTEGER,
            FOREIGN KEY (graph_id) REFERENCES graphs(id)
        );
        
        CREATE TABLE IF NOT EXISTS vertices (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            graph_id INTEGER NOT NULL,
            data TEXT,
            FOREIGN KEY (graph_id) REFERENCES graphs(id)
        );
    """.trimIndent()

        val connection = getConnection()
        connection.use { conn ->
            conn.createStatement().use { statement ->
                statement.executeUpdate(createTableSQL)
            }
        }
    }


    fun <D> insertGraph(graphVM: GraphViewModel<D>, graphName: String, graphType: String) {
        val insertGraphSQL = "INSERT INTO graphs(name, graph_type, stored_value_type) VALUES (?, ?, ?)"
        val insertEdgesSQL = "INSERT INTO edges(graph_id, vertex1_id, vertex2_id, weight) VALUES (?, ?, ?, ?)"
        val insertVerticesSQL = "INSERT INTO vertices(graph_id, data) VALUES (?, ?)"

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
                        println(vertex.data)
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



//    fun insertUser(name: String, age: Int) {
//        val insertSQL = "INSERT INTO users(name, age) VALUES (?, ?)"
//        getConnection().use { connection ->
//            connection.prepareStatement(insertSQL).use { statement ->
//                statement.setString(1, name)
//                statement.setInt(2, age)
//                statement.executeUpdate()
//            }
//        }
//    }

//    fun <D> getEdges(): List<Edge<D>> {
//        val selectSQL = "SELECT * FROM edges"
//        val users = mutableListOf<Edge<D>>()
//        getConnection().use { connection ->
//            connection.createStatement().use { statement ->
//                val resultSet = statement.executeQuery(selectSQL)
//                while (resultSet.next()) {
//                    users.add(
//                        Edge<D>(
//                            id = resultSet.getInt("id"),
//                            name = resultSet.getString("name"),
//                            age = resultSet.getInt("age")
//                        )
//                    )
//                }
//            }
//        }
//        return users
//    }
}
