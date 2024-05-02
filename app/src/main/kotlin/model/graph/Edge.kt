package model.graph

interface Edge<D> {
    val vertex1: Vertex<D>
    val vertex2: Vertex<D>
    val weight: Int
}
