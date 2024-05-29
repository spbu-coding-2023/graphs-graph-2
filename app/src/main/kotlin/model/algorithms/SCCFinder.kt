package model.algorithms

import model.graphs.DirectedGraph
import model.graphs.abstractGraph.Graph
import model.graphs.abstractGraph.Vertex

class SCCFinder {
    // SCC - Strongly Connected Components (by Kosaraju)
    fun <D> findSCC(graph: DirectedGraph<D>): MutableSet<MutableSet<Vertex<D>>> {
        val visited = mutableMapOf<Vertex<D>, Boolean>().withDefault { false }
        val stack = ArrayDeque<Vertex<D>>()
        val component = arrayListOf<Vertex<D>>()
        val sccList: MutableSet<MutableSet<Vertex<D>>> = mutableSetOf()

        fun auxiliaryDFS(srcVertex: Vertex<D>, componentList: ArrayList<Vertex<D>>) {
            visited[srcVertex] = true
            componentList.add(srcVertex)
            graph.getNeighbours(srcVertex).forEach { vertex2 ->
                if (visited[vertex2] != true) {
                    auxiliaryDFS(vertex2, componentList)
                }
            }
            stack.add(srcVertex)
        }

        graph.getVertices().forEach { vertex ->
            if (visited[vertex] != true) auxiliaryDFS(vertex, component)
        }

        val reversedEdgesMap = reverseEdgesMap(graph)
        visited.clear()
        component.clear()

        fun reverseDFS(vertex: Vertex<D>, componentList: MutableSet<Vertex<D>>) {
            visited[vertex] = true
            componentList.add(vertex)
            reversedEdgesMap[vertex]?.forEach { vertex2 ->
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

    private fun <D> reverseEdgesMap(graph: DirectedGraph<D>): Map<Vertex<D>, MutableSet<Vertex<D>>> {
        val reversedEdgesMap = mutableMapOf<Vertex<D>, MutableSet<Vertex<D>>>()
        graph.getVertices().forEach { reversedEdgesMap[it] = mutableSetOf() }
        graph.getEdges().forEach { edge ->
            reversedEdgesMap[edge.vertex2]?.add(edge.vertex1)
        }
        return reversedEdgesMap
    }
}
