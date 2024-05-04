package model

import model.edges.DirectedEdge
import model.internalGraphs._DirectedGraph

class DirectedGraph<D> : _DirectedGraph<D, DirectedEdge<D>>()
