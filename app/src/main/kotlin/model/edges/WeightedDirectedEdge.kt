package model.edges

import model.abstractGraph.Vertex

class WeightedDirectedEdge<D>(vertex1: Vertex<D>, vertex2: Vertex<D>, val weight: Int) :
    DirectedEdge<D>(vertex1, vertex2)
