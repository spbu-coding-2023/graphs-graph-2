package model.abstractGraph

class Edge<D>(val vertex1: Vertex<D>, val vertex2: Vertex<D>) {
    fun isIncident(vertex: Vertex<D>) = (vertex1 == vertex || vertex2 == vertex)

    override fun equals(other: Any?) = (other is Edge<*>) && (vertex1 == other.vertex1) && (vertex2 == other.vertex2)
}
