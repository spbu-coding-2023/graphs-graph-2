package model.io.neo4j

import model.graphs.DirectedGraph
import model.graphs.UndirectedGraph
import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
import model.graphs.abstractGraph.Graph
import model.graphs.abstractGraph.Vertex
import org.neo4j.driver.*
import java.io.Closeable

const val DIR_LABEL = "POINTS_TO"
const val UNDIR_LABEL = "CONNECTED_TO"

class Neo4jRepository(uri: String, user: String, password: String) : Closeable {
    private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))

    init {
        driver.verifyConnectivity()
    }
    private val session = driver.session()

    fun getGraphNames(): List<String> {
        val result = session.executeRead { tx ->
            tx.run(
                "MATCH (v) " +
                "RETURN " +
                "distinct labels(v) AS label"
            ).list()
        }
        println(result)

        val names = mutableListOf<String>()
        for (record in result) {
            val name = record["label"].toString().drop(2).dropLast(2)
            names.add(name)
        }

        return names
    }

    fun <D> saveOrReplaceGraph(graph: Graph<D>, name: String, isDirected: Boolean, isWeighted: Boolean) {
        clearGraph(name)

        val vertices = graph.getVertices()
        val edges = graph.getEdges()

        val weightMap = if (isWeighted) graph.getWeightMap() else null
        val edgeLabel = if (isDirected) DIR_LABEL else UNDIR_LABEL

        session.executeWrite { tx ->
            for (vertex in vertices) {
                val id = vertex.id

                val data = vertex.data.toString()

                tx.run(
                    "CREATE (:$name {id:$id, data:$data}) "
                )
            }

            for (edge in edges) {
                val v1 = edge.vertex1
                val v2 = edge.vertex2

                val id1 = v1.id
                val id2 = v2.id

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

        val graph = initializeGraph(isWeighted, isDirected)

        val graphSize = getStoredGraphSize(name)

        val wasVertexLoaded = MutableList(graphSize) { false }
        val addedVerticesList = MutableList(graphSize) { Vertex(-1, "-1") }

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

    private fun initializeGraph(isWeighted: Boolean, isDirected: Boolean): Graph<String> {
        return if (isWeighted && isDirected) WeightedDirectedGraph()
        else if (isWeighted) WeightedUndirectedGraph()
        else if (isDirected) DirectedGraph()
        else UndirectedGraph()
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
