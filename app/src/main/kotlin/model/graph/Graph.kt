package model.graph

interface Graph<D>{
    val currentId: ULong
    fun addVertex(value: D)
    fun addEdge(node1: D, node2: D)
    fun getVertices(): List<Vertex<D>>
    fun getEdges(): List<Edge<D>>
}
