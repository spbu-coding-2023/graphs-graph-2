package model.graphs.abstractGraph

class Edge<D>(val vertex1: Vertex<D>, val vertex2: Vertex<D>) {
    fun isIncident(vertex: Vertex<D>) = (vertex1 == vertex || vertex2 == vertex)
}
