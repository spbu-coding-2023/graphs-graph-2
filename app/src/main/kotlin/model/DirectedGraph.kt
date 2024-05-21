package model

import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex

open class DirectedGraph<D> : Graph<D>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        if (vertex1 == vertex2)
            throw IllegalArgumentException("Can't add edge from vertex to itself.")

        if (vertex1 !in vertices || vertex2 !in vertices)
            throw IllegalArgumentException(
                "One of the vertices (${vertex1.id}, ${vertex1.data}) and " +
                    "(${vertex2.id}, ${vertex2.data}) isn't in the graph"
            )

        // Don't do anything if the edge is already in the graph
        if (vertex2 in getNeighbours(vertex1)) return getEdge(vertex1, vertex2)

        val newEdge = Edge(vertex1, vertex2)
        edges.add(newEdge)

        outgoingEdgesMap[vertex1]?.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    override fun removeEdge(edgeToRemove: Edge<D>): Edge<D> {
        if (edgeToRemove !in edges) throw NoSuchElementException(
            "Edge between vertices (${edgeToRemove.vertex1.id}, ${edgeToRemove.vertex1.data}) and " +
                "(${edgeToRemove.vertex2.id}, ${edgeToRemove.vertex2.data}) isn't in the graph"
        )

        val vertex1 = edgeToRemove.vertex1
        val vertex2 = edgeToRemove.vertex2

        edges.remove(edgeToRemove)

        outgoingEdgesMap[vertex1]?.remove(edgeToRemove)
        adjacencyMap[vertex1]?.remove(vertex2)

        return edgeToRemove
    }

    override fun getEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        val edge = outgoingEdgesMap[vertex1]?.find { it.isIncident(vertex2) }
            ?: throw NoSuchElementException(
                "No edge between vertices (${vertex1.id}, ${vertex1.data}) and (${vertex2.id}, ${vertex2.data})"
            )

        return edge
    }

    // SCC - Strongly Connected Components (by Kosaraju)
    fun findSCC(): MutableSet<MutableSet<Vertex<D>>> {
        val visited = mutableMapOf<Vertex<D>, Boolean>().withDefault { false }
        val stack = ArrayDeque<Vertex<D>>()
        val component = arrayListOf<Vertex<D>>()
        val sccList: MutableSet<MutableSet<Vertex<D>>> = mutableSetOf()

        fun auxiliaryDFS(srcVertex: Vertex<D>, componentList: ArrayList<Vertex<D>>) {
            visited[srcVertex] = true
            componentList.add(srcVertex)
            getNeighbours(srcVertex).forEach { vertex2 ->
                if (visited[vertex2] != true) {
                    auxiliaryDFS(vertex2, componentList)
                }
            }
            stack.add(srcVertex)
        }

        vertices.forEach { vertex ->
            if (visited[vertex] != true) auxiliaryDFS(vertex, component)
        }

        reverseGraph()
        visited.clear()
        component.clear()

        fun reverseDFS(vertex: Vertex<D>, componentList: MutableSet<Vertex<D>>) {
            visited[vertex] = true
            componentList.add(vertex)
            getNeighbours(vertex).forEach { vertex2 ->
                if (visited[vertex2] != true) {
                    reverseDFS(vertex2, componentList)
                }
            }
        }

        while (stack.isNotEmpty()) {
            val vertex = stack.removeLast()
            if (visited[vertex] != true) {
                val currentComponent = mutableSetOf<Vertex<D>>()
                reverseDFS(vertex, currentComponent)
                sccList.add(currentComponent)
            }
        }
        return sccList
    }

    private fun reverseGraph() {
        val reversedAdjacencyMap = mutableMapOf<Vertex<D>, MutableSet<Vertex<D>>>()
        vertices.forEach { reversedAdjacencyMap[it] = mutableSetOf() }
        adjacencyMap.forEach { (from, toList) ->
            toList.forEach { to ->
                reversedAdjacencyMap[to]?.add(from)
            }
        }
        adjacencyMap.clear()
        adjacencyMap.putAll(reversedAdjacencyMap)
    }
}
