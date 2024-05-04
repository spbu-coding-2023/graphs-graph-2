package model

import model.edges.WeightedDirectedEdge
import model.internalGraphs._WeightedDirectedGraph

class WeightedDirectedGraph<D> : _WeightedDirectedGraph<D, WeightedDirectedEdge<D>>()
