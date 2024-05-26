package model.io.neo4j

import model.UndirectedGraph
import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Values
import java.io.Closeable

class Neo4jRepository<D>(
    uri: String,
    user: String,
    password: String
) : Closeable {

    private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))

    init {
        driver.verifyConnectivity()
    }
    private val session = driver.session()

    private val vertexToDbIdMap = mutableMapOf<Vertex<D>, Int>()
    private var nextDbId = 0

    fun addVertex(vertex: Vertex<D>) {
        vertexToDbIdMap[vertex] = nextDbId
        val id = nextDbId++

        val data = vertex.data.toString()

        session.executeWrite {
            it.run(
                "CREATE (:Vertex {id:\$id, data:\$data})",
                Values.parameters("id", id, "data", data)
            )
        }
    }

    fun addEdge(edge: Edge<D>) {
        val v1 = edge.vertex1
        val v2 = edge.vertex2

        val id1 = vertexToDbIdMap[v1]
        val id2 = vertexToDbIdMap[v2]

        val data1 = v1.data.toString()
        val data2 = v2.data.toString()

        session.executeWrite {
            it.run(
                "MATCH (v:Vertex {id:\$id1, data:\$data1}) " +
                "MATCH (u:Vertex {id:\$id2, data:\$data2}) " +
                "CREATE (v)-[e:Edge]->(u)",

                Values.parameters(
                    "id1", id1, "id2", id2,
                    "data1", data1, "data2", data2
                )
            )
        }
    }

    override fun close() {
        session.close()
        driver.close()
    }
}
