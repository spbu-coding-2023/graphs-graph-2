package model.internalGraphs

import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import model.edges.DirectedEdge

abstract class _DirectedGraph<D, E : DirectedEdge<D>> : Graph<D, E>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): E {
        if (vertex1 == vertex2) throw IllegalArgumentException("Vertices are the same")
        if (vertex1 !in adjacencyMap.keys || vertex2 !in adjacencyMap.keys)
            throw IllegalArgumentException("Vertex1 or vertex2 are not in the graph")

        val newEdge = DirectedEdge(vertex1, vertex2) as E
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    override fun removeEdge(edgeToRemove: E): E {
        if (edgeToRemove !in edges) throw IllegalArgumentException("Edge is not in the graph")

        val vertex1 = edgeToRemove.vertex1
        val vertex2 = edgeToRemove.vertex2

        adjacencyMap[vertex1]?.remove(vertex2)
        edges.remove(edgeToRemove)

        return edgeToRemove
    }

    fun findSCC() { // SCC - Strongly Connected Components (by Kosaraju)
        val visited = mutableMapOf<Vertex<D>, Boolean>().withDefault { false }
        val stack = mutableListOf<Vertex<D>>()
        val component = mutableListOf<Vertex<D>>()

        fun auxuiliaryDFS(srcVertex: Vertex<D>, componentList:MutableList<Vertex<D>>) {
            visited[srcVertex] = true
            componentList.add(srcVertex)
            adjacencyMap[srcVertex]?.forEach { vertex2 ->
                if (visited[vertex2] != null && visited[vertex2] != true) {
                    auxuiliaryDFS(vertex2, componentList)
                }
            }
            stack.add(srcVertex)
        }

        for (vertex in adjacencyMap.keys) {
            if (visited[vertex] != null && visited[vertex] != true) {
                auxuiliaryDFS(vertex, component)
            }
        }

        reverseGraph()
        visited.clear()
        component.clear()

        while(stack.isNotEmpty()) {
            val vertex = stack.removeAt(stack.size - 1)
            if (visited[vertex] != null && visited[vertex] != true) {
                val currentComponent = mutableListOf<Vertex<D>>()
                auxuiliaryDFS(vertex, currentComponent)
                println("Component: $currentComponent")
            }
        }
    }

    private fun reverseGraph() {
        val reversedAdjacencyMap = mutableMapOf<Vertex<D>, ArrayList<Vertex<D>>>()
        for (vertex in adjacencyMap.keys) {
            adjacencyMap[vertex]?.forEach { vertex2 ->
                reversedAdjacencyMap[vertex2] = reversedAdjacencyMap[vertex2] ?: ArrayList()
                reversedAdjacencyMap[vertex2]?.add(vertex)
            }
        }
        adjacencyMap.clear()
        adjacencyMap.putAll(reversedAdjacencyMap)
    }
}
