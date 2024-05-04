package model

import model.edges.UndirectedEdge
import model.internalGraphs._UndirectedGraph

class UndirectedGraph<D> : _UndirectedGraph<D, UndirectedEdge<D>>()
