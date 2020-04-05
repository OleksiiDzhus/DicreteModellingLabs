package com.joske.discretemodels.labs.labone

import java.util.*

data class Edge(var source: Int, var destination: Int, var weight: Int) : Comparable<Edge> {
    override fun compareTo(other: Edge): Int {
        return weight - other.weight
    }

    override fun toString(): String {
        return "[Source:$source, Destination: $destination, weight: $weight]"
    }
}

data class Graph(
    val vertices: Int,
    val edges: Queue<Edge> = PriorityQueue()
) {
    private val parent: IntArray = IntArray(vertices) { it }

    fun addEdge(source: Int, destination: Int, weight: Int): Boolean {
        val edge = Edge(source, destination, weight)
        return edges.add(edge)
    }

    // chain of parent pointers from x upwards through the tree
    // until an element is reached whose parent is itself
    private fun find(vertex: Int): Int =
        if (parent[vertex] != vertex) find(parent[vertex]) else vertex

    private fun union(x: Int, y: Int) {
        val xSetParent = find(x)
        val ySetParent = find(y)
        //make x as parent of y
        parent[ySetParent] = xSetParent
    }

    fun kruskalMinimumSpanningTree(): List<Edge> {
        val mst = ArrayList<Edge>()

        with(copy()) {
            edges.forEach {
                run {
                    val xSet = find(it.source)
                    val ySet = find(it.destination)

                    if (xSet != ySet) {
                        mst.add(it)
                        union(xSet, ySet)
                    }
                }
            }
        }


        return mst
    }

    fun kruskalMaximumSpanningTree(): List<Edge> {
        val mst = ArrayList<Edge>()

        with(copy()) {
            edges.sortedByDescending { it.weight }
                .forEach {
                    run {
                        val xSet = find(it.source)
                        val ySet = find(it.destination)

                        if (xSet != ySet) {
                            mst.add(it)
                            union(xSet, ySet)
                        }
                    }
                }
        }

        return mst.sortedDescending()
    }

    fun print() {
        edges.sorted().forEach(::println)
    }

}


fun main() {
    val vertices = 5
    val graph = Graph(vertices)

    graph.addEdge(0, 1, 1)
    graph.addEdge(0, 2, 5)
    graph.addEdge(0, 3, 3)
    graph.addEdge(0, 4, 4)
    graph.addEdge(1, 2, 7)
    graph.addEdge(2, 3, 6)
    graph.addEdge(3, 4, 2)

    println("Вхідний граф")

    graph.print()

    println("Мінімальне покриваюче дерево")

    graph.kruskalMinimumSpanningTree().forEach(::println)

    println("Максимальне покриваюче дерево")

    graph.kruskalMaximumSpanningTree().forEach(::println)
}


