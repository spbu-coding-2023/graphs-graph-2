package model

import model.edges.WeightedUndirectedEdge
import model.internalGraphs._WeightedUndirectedGraph

class WeightedUndirectedGraph<D> : _WeightedUndirectedGraph<D, WeightedUndirectedEdge<D>>()
