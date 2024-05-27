package model.io.neo4j

import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import java.io.Closeable

const val DIR_LABEL = "POINTS_TO"
const val UNDIR_LABEL = "CONNECTED_TO"

class Neo4jRepository<D>(uri: String, user: String, password: String) : Closeable {
    private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
    private val session = driver.session()

    private val vertexToIdMap = mutableMapOf<Vertex<D>, Int>()
    private var nextId = 0

    fun saveGraph(graph: Graph<D>, name: String, isDirected: Boolean, isWeighted: Boolean) {
        val vertices = graph.getVertices()
        val edges = graph.getEdges()

        val weightMap = if (isWeighted) graph.getWeightMap() else null
        val edgeLabel = if (isDirected) DIR_LABEL else UNDIR_LABEL

        session.executeWrite { tx ->
            for (vertex in vertices) {
                vertexToIdMap[vertex] = nextId
                val id = nextId++

                val data = vertex.data.toString()

                tx.run(
                    "CREATE (:$name {id:$id, data:$data}) "
                )
            }

            for (edge in edges) {
                val v1 = edge.vertex1
                val v2 = edge.vertex2

                val id1 = vertexToIdMap[v1]
                val id2 = vertexToIdMap[v2]

                val data1 = v1.data.toString()
                val data2 = v2.data.toString()

                val weight = weightMap?.get(edge)

                tx.run(
                    "MATCH (v:$name {id:$id1, data:$data1}) " +
                    "MATCH (u:$name {id:$id2, data:$data2}) " +
                    "CREATE (v)-[:$edgeLabel {weight:$weight}]->(u) "
                )
            }
        }
    }

    override fun close() {
        session.close()
        driver.close()
    }
}
