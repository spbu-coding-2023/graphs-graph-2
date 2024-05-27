package model.algorithms.clustering.implementation

interface Link {
    fun source(): Int
    fun target(): Int
    fun weight(): Double
}
