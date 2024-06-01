/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package model.algorithms.clustering.implementation

/** Simple link representation used inside Node and Louvain algorithm. */
internal class InternalLink(val to: Int, val weight: Double)

/**
 * @param community Index of a community to which the node has been assigned.
 * @param selfLoopsWeight Weight of all links that start and end at originalNodes making up this node * 2.
 */
internal sealed class BaseNode(
    var community: Int,
    val originalNodes: Set<Int>,
    open val incidentLinks: List<InternalLink>,
    open val selfLoopsWeight: Double
) {
    fun neighbourCommunities(nodes: List<Node>) =
        incidentLinks.map { nodes[it.to].community }.distinct().filter { it != community }
}

internal class Node(
    community: Int,
    originalNodes: Set<Int>,
    incidentLinks: List<InternalLink>,
    selfLoopsWeight: Double = 0.0
) : BaseNode(community, originalNodes, incidentLinks, selfLoopsWeight) {
    val outDegree = incidentLinks.sumOf { it.weight }

    fun degree() = outDegree + selfLoopsWeight
}

internal class MutableNode(
    community: Int,
    originalNodes: Set<Int>,
    override val incidentLinks: MutableList<InternalLink> = mutableListOf(),
    override var selfLoopsWeight: Double = 0.0
) : BaseNode(community, originalNodes, incidentLinks, selfLoopsWeight) {
    fun toNode(): Node = Node(community, originalNodes, incidentLinks)
}
