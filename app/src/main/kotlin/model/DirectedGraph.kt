package model

import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex

open class DirectedGraph<D> : Graph<D>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        if (vertex1 == vertex2)
            throw IllegalArgumentException("Can't add edge from vertex to itself.")

        if (vertex1 !in vertices || vertex2 !in vertices)
            throw NoSuchElementException("Vertex1 or vertex2 is not in the vertices array.")

        val newEdge = Edge(vertex1, vertex2)
        edges.add(newEdge)

        outEdgesMap[vertex1]?.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    override fun removeEdge(edgeToRemove: Edge<D>): Edge<D> {
        if (edgeToRemove !in edges)
            throw NoSuchElementException("Edge is not in the graph")

        val vertex1 = edgeToRemove.vertex1
        val vertex2 = edgeToRemove.vertex2

        edges.remove(edgeToRemove)

        outEdgesMap[vertex1]?.remove(edgeToRemove)
        adjacencyMap[vertex1]?.remove(vertex2)

        return edgeToRemove
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

