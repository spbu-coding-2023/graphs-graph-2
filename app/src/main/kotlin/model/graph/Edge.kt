package model.graph

interface Edge<V> {
    val vertex1: Vertex<V>?
    val vertex2: Vertex<V>?
}
