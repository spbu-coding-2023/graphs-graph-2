package model.edges

import model.abstractGraph.Edge
import model.abstractGraph.Vertex

open class UndirectedEdge<D>(override val vertex1: Vertex<D>, override val vertex2: Vertex<D>) : Edge<D>