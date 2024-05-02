package model.edges

import model.abstractGraph.Vertex

class WeightedUndirectedEdge<D>(vertex1: Vertex<D>, vertex2: Vertex<D>, val weight: Int) :
    UndirectedEdge<D>(vertex1, vertex2)
