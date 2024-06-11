package model.algorithms

import model.graphs.DirectedGraph
import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Vertex

class CyclesFinder {
    fun <D> findCycles(graph: DirectedGraph<D>, srcVertex: Vertex<D>): Set<List<Pair<Edge<D>, Vertex<D>>>> {
        if (graph.getOutgoingEdges(srcVertex).isEmpty()) return emptySet()

        val SCCFinder = SCCFinder()
        val sccs = SCCFinder.findSCC(graph)
        val vertexSCC = sccs.find { srcVertex in it }
            ?: throw NoSuchElementException("Vertex (${srcVertex.id}, ${srcVertex.data}) isn't in any of the SCCs")

        if (vertexSCC.size == 1) return emptySet()

        // create SCC subgraph
        val subGraph = DirectedGraph<D>()

        // map to restore original vertices
        val verticesCopiesMap = mutableMapOf<Vertex<D>, Vertex<D>>()

        for (originalVertex in vertexSCC) {
            val vertexCopy = subGraph.addVertex(originalVertex.data)
            verticesCopiesMap[vertexCopy] = originalVertex
        }

        for (edge in graph.getEdges()) {
            if (edge.vertex1 in vertexSCC && edge.vertex2 in vertexSCC) {
                val vertex1copy = verticesCopiesMap.filterValues { it == edge.vertex1 }.keys.toList()[0]
                val vertex2copy = verticesCopiesMap.filterValues { it == edge.vertex2 }.keys.toList()[0]

                subGraph.addEdge(vertex1copy, vertex2copy)
            }
        }

        val copyOfSrcVertex = verticesCopiesMap.filterValues { it == srcVertex }.keys.toList()[0]

        val blockedSet = mutableSetOf<Vertex<D>>()
        val blockedMap = mutableMapOf<Vertex<D>, MutableSet<Vertex<D>>>()
        val stack = ArrayDeque<Vertex<D>>()
        val verticesCycles = mutableSetOf<List<Vertex<D>>>()

        fun DFSToFindCycles(currentVertex: Vertex<D>): Boolean {
            var cycleIsFound = false

            stack.addLast(currentVertex)
            blockedSet.add(currentVertex)

            for (neighbour in subGraph.getNeighbours(currentVertex)) {
                if (neighbour == copyOfSrcVertex) {
                    // cycle is found
                    stack.addLast(copyOfSrcVertex)

                    val cycleOfVertices = mutableListOf<Vertex<D>>()
                    cycleOfVertices.addAll(stack)
                    verticesCycles.add(cycleOfVertices)

                    stack.removeLast()

                    cycleIsFound = true
                } else if (neighbour !in blockedSet) {
                    // next iteration
                    cycleIsFound = DFSToFindCycles(neighbour) || cycleIsFound
                }
            }

            fun unblock(vertex: Vertex<D>) {
                blockedSet.remove(vertex)
                blockedMap[vertex]?.forEach { unblock(it) }
                blockedMap.remove(vertex)
            }

            if (cycleIsFound) unblock(currentVertex)
            else {
                for (neighbour in subGraph.getNeighbours(currentVertex)) {
                    blockedMap[neighbour]?.add(currentVertex)
                        ?: blockedMap.put(neighbour, mutableSetOf(currentVertex))
                }
            }

            stack.removeLast()

            return cycleIsFound
        }

        DFSToFindCycles(copyOfSrcVertex)

        val cycles = mutableSetOf<List<Pair<Edge<D>, Vertex<D>>>>()
        for (verticesCycle in verticesCycles) {
            val originalVerticesCycle = mutableListOf<Vertex<D>>()
            for (vertex in verticesCycle) originalVerticesCycle += verticesCopiesMap[vertex]
                ?: throw NoSuchElementException("Vertex (${vertex.id}, ${vertex.data}) isn't in the vertices map")

            val cycle = mutableListOf<Pair<Edge<D>, Vertex<D>>>()

            for (i in 0..originalVerticesCycle.size - 2) {
                val v1 = originalVerticesCycle[i]
                val v2 = originalVerticesCycle[i + 1]

                cycle.add(graph.getEdge(v1, v2) to v2)
            }

            cycles.add(cycle)
        }

        return cycles
    }
}
