package model

import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex

open class DirectedGraph<D> : Graph<D>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        if (vertex1 == vertex2)
            throw IllegalArgumentException("Can't add edge from vertex to itself.")

        if (vertex1.id > vertices.size || vertex2.id > vertices.size)
            throw NoSuchElementException(
                "One of vertices (${vertex1.id}, ${vertex1.data}) and " +
                    "(${vertex2.id}, ${vertex2.data}) is not in the vertices array."
            )

        val newEdge = Edge(vertex1, vertex2)
        edges.add(newEdge)

        outgoingEdgesMap[vertex1]?.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    override fun removeEdge(edgeToRemove: Edge<D>): Edge<D> {
        if (edgeToRemove !in edges) throw NoSuchElementException(
            "Edge between vertices (${edgeToRemove.vertex1.id}, ${edgeToRemove.vertex1.data}) and " +
                "(${edgeToRemove.vertex2.id}, ${edgeToRemove.vertex2.data}) is not in the graph"
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
    fun findSCC(): ArrayList<ArrayList<Vertex<D>>> {
        val visited = mutableMapOf<Vertex<D>, Boolean>().withDefault { false }
        val stack = ArrayDeque<Vertex<D>>()
        val component = arrayListOf<Vertex<D>>()
        val sccList: ArrayList<ArrayList<Vertex<D>>> = arrayListOf()

        fun auxiliaryDFS(srcVertex: Vertex<D>, componentList: ArrayList<Vertex<D>>) {
            visited[srcVertex] = true
            componentList.add(srcVertex)
            adjacencyMap[srcVertex]?.forEach { vertex2 ->
                if (visited[vertex2] != null && visited[vertex2] != true) {
                    auxiliaryDFS(vertex2, componentList)
                }
            }
            stack.add(srcVertex)
        }

        for (vertex in vertices) {
            if (visited[vertex] != null && visited[vertex] != true) auxiliaryDFS(vertex, component)
        }

        reverseGraph()
        visited.clear()
        component.clear()

        while (stack.isNotEmpty()) {
            val vertex = stack.removeLast()
            if (visited[vertex] != null && visited[vertex] != true) {
                val currentComponent = arrayListOf<Vertex<D>>()
                auxiliaryDFS(vertex, currentComponent)
                sccList.add(currentComponent)
            }
        }
        return sccList
    }

    private fun reverseGraph() {
        val reversedAdjacencyMap = mutableMapOf<Vertex<D>, ArrayList<Vertex<D>>>()
        for (vertex in vertices) {
            adjacencyMap[vertex]?.forEach { vertex2 ->
                reversedAdjacencyMap[vertex2] = reversedAdjacencyMap[vertex2] ?: ArrayList()
                reversedAdjacencyMap[vertex2]?.add(vertex)
            }
        }
        adjacencyMap.clear()
        adjacencyMap.putAll(reversedAdjacencyMap)
    }
}
