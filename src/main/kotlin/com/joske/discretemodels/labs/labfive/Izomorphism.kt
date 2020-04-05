package com.joske.discretemodels.labs.labfive

import java.io.FileReader

const val ZN = 8
const val ZE = 12
const val N = 8
var general = false

fun main() {
    izomorphizm()
    if (general) println("Графи ізоморфні") else println("Графи не ізоморфні")
}

fun swapLeafs(mat: Array<BooleanArray>, x: Int, y: Int): Array<BooleanArray> {
    for (i in mat.indices) {
        val k = mat[i][x]
        mat[i][x] = mat[i][y]
        mat[i][y] = k
    }
    for (j in mat.indices) {
        val k = mat[x][j]
        mat[x][j] = mat[y][j]
        mat[y][j] = k
    }
    return mat
}

fun ETS(oneGraf: Array<BooleanArray?>, twoGraf: Array<BooleanArray>): Boolean {
    for (i in oneGraf.indices) {
        for (j in i until oneGraf.size) {
            if (oneGraf.contentEquals(swapLeafs(twoGraf, i, j))) return true
        }
    }
    return false
}

fun izomorphizm() {
    val oneInc = MatGraph(ZN)
    val twoInc = MatGraph(ZN)
    val oneGraf = arrayOfNulls<Eji>(ZE)
    val twoGraf = arrayOfNulls<Eji>(ZE)

    FileReader("C:\\education-workspace\\DiscreteModelsLabs\\src\\main\\kotlin\\com\\joske\\discretemodels\\labs\\labfive\\iso1").use { reader ->
        var i = 0
        var c: Int
        var numbOut = 0
        var numbIn = 0
        while (reader.read().also { c = it } != -1) {

            if (c.toChar() == '\n') {
                oneGraf[i] = Eji(numbOut, numbIn)
                i++
            } else if (c >= 65) {
                numbOut = c.toChar().toLowerCaseInt()

                while (reader.read().also { c = it } < 65);
                numbIn = c.toChar().toLowerCaseInt()
            }
        }
    }

    for (i in 0 until ZE) {
        oneInc.mat[oneGraf[i]!!.A][oneGraf[i]!!.B] = true
        oneInc.mat[oneGraf[i]!!.B][oneGraf[i]!!.A] = true
    }
    FileReader("C:\\education-workspace\\DiscreteModelsLabs\\src\\main\\kotlin\\com\\joske\\discretemodels\\labs\\labfive\\iso3").use { reader ->
        var i = 0
        var c: Int
        var numbOut = 0
        var numbIn = 0
        while (reader.read().also { c = it } != -1) {
            if (c.toChar() == '\n') {
                twoGraf[i] = Eji(numbOut, numbIn)
                i++
            } else if (c >= 65) {
                numbOut = c.toChar().toLowerCaseInt()
                while (reader.read().also { c = it } < 65);
                numbIn = c.toChar().toLowerCaseInt()
            }
        }
    }

    for (i in 0 until ZE) {
        twoInc.mat[twoGraf[i]!!.A][twoGraf[i]!!.B] = true
        twoInc.mat[twoGraf[i]!!.B][twoGraf[i]!!.A] = true
    }
    oneInc.antiflex(twoInc, N - 1)
}

class Eji(var A: Int, var B: Int)

fun Char.toLowerCaseInt() =
    toInt() - 65

class MatGraph(private var size: Int) {
    var mat: Array<BooleanArray> = Array(size) { BooleanArray(size) }

    fun equals(other: MatGraph): Boolean {
        if (other.size != size) return false
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (other.mat[i][j] != mat[i][j]) return false
            }
        }
        return true
    }

    fun reverse(p: MatGraph, m: Int) {
        var i = 0
        var j = m
        while (i < j) {
            p.swapLeafs(i, j)
            ++i
            --j
        }
    }

    fun antiflex(p: MatGraph, m: Int) {
        var i: Int
        if (m == 0) {
            if (equals(p)) {
                general = true
            }
        } else {
            i = 0
            while (i <= m) {
                antiflex(p, m - 1)
                if (i < m) {
                    p.swapLeafs(i, m)
                    reverse(p, m - 1)
                }
                ++i
            }
        }
    }

    private fun swapLeafs(x: Int, y: Int) {
        for (i in 0 until size) {
            val k = mat[i][x]
            mat[i][x] = mat[i][y]
            mat[i][y] = k
        }
        for (j in 0 until size) {
            val k = mat[x][j]
            mat[x][j] = mat[y][j]
            mat[y][j] = k
        }
    }
}
