package model.abstractGraph

abstract class Edge<D> {
    abstract val vertex1: Vertex<D>
    abstract val vertex2: Vertex<D>
}
