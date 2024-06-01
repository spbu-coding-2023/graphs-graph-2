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

/** This file was changed compared to the source (one public method was deleted) */
package model.algorithms.clustering.implementation

/**
 * Runs Louvain algorithm to approximate the best modularity partition and returns a corresponding mapping of nodes to
 * communities. If depth > 0 then algorithm tries to split large communities into smaller ones depth times recursively.
 *
 * @param depth Number of attempts to split large communities
 * @return Map: nodeIndex -> communityIndex
 */
fun getPartition(links: List<Link>, depth: Int = 0): Map<Int, Int> {
    val louvain = Louvain(links)
    louvain.optimizeModularity(depth)
    return louvain.resultingCommunities()
}
