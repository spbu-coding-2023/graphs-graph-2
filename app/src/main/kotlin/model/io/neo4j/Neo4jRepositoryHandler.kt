package model.io.neo4j

import model.graphs.abstractGraph.Graph
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase

object Neo4jRepositoryHandler {
    var neo4jRepo: Neo4jRepository? = null
        private set

    var isRepoInit = false
        private set

    fun initRepo(uri: String, user: String, password: String): Boolean {
        if (isRepoInit) return true
        if (!checkIfCredentialsAreValid(uri, user, password)) return false

        neo4jRepo = Neo4jRepository(uri, user, password)
        isRepoInit = true

        return true
    }

    fun getNames(): List<String>? {
        if (!isRepoInit) return null

        val names = neo4jRepo?.getGraphNames()

        return names
    }

    fun <D> saveOrReplace(graph: Graph<D>, name: String, isDirected: Boolean, isWeighted: Boolean): Boolean {
        if (!isRepoInit || !isValidNeo4jName(name)) return false

        neo4jRepo?.saveOrReplaceGraph(graph, name, isDirected, isWeighted)

        return true
    }

    fun loadGraph(name: String): Triple<Graph<String>, Boolean, Boolean>? {
        if (!isRepoInit || !isValidNeo4jName(name)) return null

        val graphWithInfo = neo4jRepo?.loadGraph(name) ?: return null
        val graph = graphWithInfo.first
        val isWeighed = graphWithInfo.second
        val isDirected = graphWithInfo.third

        return Triple(graph, isWeighed, isDirected)
    }

    fun isValidNeo4jName(name: String): Boolean {
        if (name.isEmpty()) return false
        for (i in name.indices) {
            val isValidChar =
                when (name[i].code) {
                    45 -> { // -
                        if (i != 0) true else false
                    }
                    in 48..57 -> { // 0-9
                        if (i != 0) true else false
                    }
                    in 65..90 -> true // A-Z
                    95 -> true // _
                    in 97..122 -> true // a-z
                    else -> false
                }

            if (isValidChar) continue else return false
        }

        return true
    }

    private fun checkIfCredentialsAreValid(uri: String, user: String, password: String): Boolean {
        try {
            val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
            driver.session().executeWrite { tx -> tx.run { "MATCH (v) RETURN v LIMIT 1" } }
        } catch (e: Exception) {
            return false
        }

        return true
    }
}
