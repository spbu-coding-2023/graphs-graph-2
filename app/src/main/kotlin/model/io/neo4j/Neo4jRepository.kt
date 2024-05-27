package model.io.neo4j

import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Record
import java.io.Closeable

const val DIR_LABEL = "POINTS_TO"
const val UNDIR_LABEL = "CONNECTED_TO"

class Neo4jRepository<D>(uri: String, user: String, password: String) : Closeable {
    private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
    private val session = driver.session()

    private val vertexToIdMap = mutableMapOf<Vertex<D>, Int>()
    private var nextId = 0

    fun saveOrReplaceGraph(graph: Graph<D>, name: String, isDirected: Boolean, isWeighted: Boolean) {
        clearGraph(name)

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

    private fun clearGraph(name: String) {
        session.executeWrite { tx ->
            tx.run(
                "MATCH (v:$name)-[e]->(u:$name) " +
                "DELETE v, e, u "
            )
        }
    }

    fun loadGraph(name: String): Graph<String> {
        val graphContents = readGraphContents(name)

        val isDirected = hasDirection(graphContents[0])
        val isWeighted = hasWeight(graphContents[0])

        val graph: Graph<String> =
            if (isWeighted && isDirected) WeightedDirectedGraph()
            else if (isWeighted) WeightedUndirectedGraph()
            else if (isDirected) DirectedGraph()
            else UndirectedGraph()

        val graphSize = getStoredGraphSize(name)

        val wasVertexLoaded = MutableList(graphSize) { false }
        val addedVerticesList = MutableList<Vertex<String>?>(graphSize) { null }

        for (content in graphContents) {
            val id1 = content["id1"].asInt()
            val data1 = content["data1"].toString()

            val id2 = content["id2"].asInt()
            val data2 = content["data2"].toString()

            val weightString = content["weight"].toString()
            val edgeWeight = if (weightString != "NULL") weightString.toInt() else null

            if (!wasVertexLoaded[id1]) {
                wasVertexLoaded[id1] = true
                addedVerticesList[id1] = graph.addVertex(data1)
            }
            if (!wasVertexLoaded[id2]) {
                wasVertexLoaded[id2] = true
                addedVerticesList[id2] = graph.addVertex(data2)
            }

            val v1 = addedVerticesList[id1]
            val v2 = addedVerticesList[id2]

            if (v1 == null)
                throw IllegalStateException(
                    "Vertex with database id $id1 and data $data1 wasn't loaded from database, " +
                    "but was marked as loaded. Cannot continue graph import."
                )
            if (v2 == null)
                throw IllegalStateException(
                    "Vertex with database id $id2 and data $data2 wasn't loaded from database, " +
                    "but was marked as loaded. Cannot continue graph import."
                )

            if (edgeWeight == null) {
                graph.addEdge(v1, v2)
            } else if (isDirected) {
                graph as WeightedDirectedGraph
                graph.addEdge(v1, v2, edgeWeight)
            } else {
                graph as WeightedUndirectedGraph
                graph.addEdge(v1, v2, edgeWeight)
            }
        }

        return graph
    }

    private fun readGraphContents(name: String) = session.executeRead { tx ->
        tx.run(
            "MATCH (v:$name)-[e]->(u:$name) " +
            "RETURN " +
            "v.id AS id1, " +
            "v.data AS data1, " +
            "u.id AS id2, " +
            "u.data AS data2, " +
            "e.weight AS weight, " +
            "type(e) AS relationType "
        ).list()
    }

    private fun getStoredGraphSize(name: String): Int {
        val result = session.executeRead { tx ->
            tx.run(
                "MATCH (v:$name) " +
                "RETURN count(v) AS size "
            ).list()
        }

        val size = result[0]["size"].asInt()

        return size
    }

    private fun hasWeight(graphRecord: Record): Boolean {
        return graphRecord["weight"].toString() != "NULL"
    }

    private fun hasDirection(graphRecord: Record): Boolean {
        return graphRecord["relationType"].asString() == DIR_LABEL
    }

    override fun close() {
        session.close()
        driver.close()
    }
}
