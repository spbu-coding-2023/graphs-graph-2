package model.abstractGraph

class Vertex<D>(val id: Int, val data: D) {
    override fun equals(other: Any?) = (other is Vertex<*>) && (id == other.id) && (data == other.data)
}
