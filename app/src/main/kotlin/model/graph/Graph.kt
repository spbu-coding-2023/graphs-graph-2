package model.graph

abstract class Graph<V, EdgeType>{
    //val adjacencyMap: MutableMap<Vertex<V>, ArrayList<Any>>

    abstract fun addVertex(value: V)
    abstract fun addEdge(node1: V, node2: V)
    abstract fun getVertices(): List<Vertex<V>>
    abstract fun getEdges(): List<EdgeType>
}
