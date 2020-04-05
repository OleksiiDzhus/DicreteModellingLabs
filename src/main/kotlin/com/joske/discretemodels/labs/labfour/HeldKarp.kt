package com.joske.discretemodels.labs.labfour

import java.io.File

private val distances: ArrayList<IntArray> = ArrayList()
private var optimalDistance = Int.MAX_VALUE
private var optimalPath = ""

fun main() {

    println("Введіть шлях до файлу")
    val filePath: String = readLine()!!

    File(filePath).useLines {
        it.forEach { e ->
            distances.add(
                e.trim(' ')
                    .split("\\s+".toRegex())
                    .map { i -> i.toInt() }
                    .toIntArray()
            )
        }
    }

    // Initial variables to start the algorithm
    val path = ""
    val vertices = IntArray(distances.size - 1)

    // Filling the initial vertices array with the proper values
    for (i in 1 until distances.size) {
        vertices[i - 1] = i
    }

    heldKarp(0, vertices, path, 0)
    print("Шлях: $optimalPath.\nМінімальна вага = $optimalDistance")
}

private fun heldKarp(initial: Int, vertices: IntArray, path: String, costUntilHere: Int): Int {

    // We concatenate the current path and the vertex taken as initial
    var currentPath = path
    currentPath = "$currentPath$initial - "
    val length = vertices.size
    var newCostUntilHere: Int

    // Exit case, if there are no more options to evaluate (last node)
    return if (length == 0) {
        newCostUntilHere = costUntilHere + distances[initial][0]

        // If its cost is lower than the stored one
        if (newCostUntilHere < optimalDistance) {
            optimalDistance = newCostUntilHere
            optimalPath = currentPath + "0"
        }
        distances[initial][0]
    } else if (costUntilHere > optimalDistance) {
        0
    } else {
        val newVertices = Array(length) { IntArray(length - 1) }
        var costCurrentNode: Int
        var costChild: Int
        var bestCost = Int.MAX_VALUE

        // For each of the nodes of the list
        for (i in 0 until length) {

            // Each recursion new vertices list is constructed
            var j = 0
            var k = 0
            while (j < length) {

                // The current child is not stored in the new vertices array
                if (j == i) {
                    k--
                    j++
                    k++
                    continue
                }
                newVertices[i][k] = vertices[j]
                j++
                k++
            }

            // Cost of arriving the current node from its parent
            costCurrentNode = distances[initial][vertices[i]]

            // Here the cost to be passed to the recursive function is computed
            newCostUntilHere = costCurrentNode + costUntilHere

            // RECURSIVE CALLS TO THE FUNCTION IN ORDER TO COMPUTE THE COSTS
            costChild = heldKarp(vertices[i], newVertices[i], currentPath, newCostUntilHere)

            // The cost of every child + the current node cost is computed
            val totalCost = costChild + costCurrentNode

            // Finally we select from the minimum from all possible children costs
            if (totalCost < bestCost) {
                bestCost = totalCost
            }
        }
        bestCost
    }
}
