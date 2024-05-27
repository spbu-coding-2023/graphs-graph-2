package model.algorithms.clustering.implementation

/**
 * Runs Louvain algorithm to approximate the best modularity partition and returns a corresponding mapping of nodes to communities.
 * If depth > 0 then algorithm tries to split large communities into smaller ones depth times recursively.
 *
 * @param depth Number of attempts to split large communities
 * @return Map: nodeIndex -> communityIndex
 */
fun getPartition(links: List<Link>, depth: Int = 0): Map<Int, Int> {
    val louvain = Louvain(links)
    louvain.optimizeModularity(depth)
    return louvain.resultingCommunities()
}

