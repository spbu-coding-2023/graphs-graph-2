package model.graphs.abstractGraph

import java.util.*
import java.util.ArrayDeque
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

abstract class Graph<D> {
    protected val vertices: ArrayList<Vertex<D>> = arrayListOf()
    protected val edges: MutableSet<Edge<D>> = mutableSetOf()

    protected val adjacencyMap: MutableMap<Vertex<D>, MutableSet<Vertex<D>>> = mutableMapOf()
    protected val outgoingEdgesMap: MutableMap<Vertex<D>, MutableSet<Edge<D>>> = mutableMapOf()

    private var nextId = 0

    fun addVertex(data: D): Vertex<D> {
        val newVertex = Vertex(nextId++, data)

        outgoingEdgesMap[newVertex] = mutableSetOf()
        adjacencyMap[newVertex] = mutableSetOf()

        vertices.add(newVertex)

        return newVertex
    }

    fun removeVertex(vertexToRemove: Vertex<D>): Vertex<D> {
        nextId--

        val adjacentVertices = getNeighbours(vertexToRemove)
        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToRemove)

        // iterator is used because an element can't be removed in a for loop
        val iterator = edges.iterator()
        while (iterator.hasNext()) {
            val edge = iterator.next()
            if (edge.isIncident(vertexToRemove)) {
                iterator.remove()

                val incidentVertex = if (edge.vertex1 == vertexToRemove) edge.vertex2 else edge.vertex1
                outgoingEdgesMap[incidentVertex]?.remove(edge)
                adjacencyMap[incidentVertex]?.remove(vertexToRemove)
            }
        }

        val lastAddedVertex = vertices[nextId]
        lastAddedVertex.id = vertexToRemove.id
        vertices[vertexToRemove.id] = lastAddedVertex

        vertices.remove(vertexToRemove)
        adjacencyMap.remove(vertexToRemove)
        outgoingEdgesMap.remove(vertexToRemove)

        return vertexToRemove
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>

    abstract fun removeEdge(edgeToRemove: Edge<D>): Edge<D>

    fun getEdges() = edges.toList()

    fun getVertices() = vertices.toList()

    /* In undirected graph, returns a map with every edge as a key and 1 as a value
     * In a directed graph, returns copy of weightMap property */
    open fun getWeightMap(): MutableMap<Edge<D>, Int> {
        val weightMap = mutableMapOf<Edge<D>, Int>()

        for (edge in edges) weightMap[edge] = 1

        return weightMap
    }

    fun getNeighbours(vertex: Vertex<D>): List<Vertex<D>> {
        val neighbours = adjacencyMap[vertex]
            ?: throw NoSuchElementException("Vertex (${vertex.id}, ${vertex.data}) isn't in the adjacency map.")

        return neighbours.toList()
    }

    fun getOutgoingEdges(vertex: Vertex<D>): List<Edge<D>> {
        val outgoingEdges = outgoingEdgesMap[vertex]
            ?: throw NoSuchElementException("Vertex (${vertex.id}, ${vertex.data}) isn't in the outgoing edges map.")

        return outgoingEdges.toList()
    }

    abstract fun getEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>

    open fun hasNegativeEdges() = false
}
