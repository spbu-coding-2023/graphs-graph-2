package model.abstractGraph

interface Edge<D> {
    val vertex1: Vertex<D>
    val vertex2: Vertex<D>
}
