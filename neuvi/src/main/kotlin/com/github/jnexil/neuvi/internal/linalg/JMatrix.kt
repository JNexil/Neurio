package com.github.jnexil.neuvi.internal.linalg

import com.github.jnexil.neuvi.api.linalg.*
import java.io.*
import java.util.Arrays.*

internal class JMatrix(override val rows: Int, override val columns: Int): MutableMatrix, Serializable {
    private val matrix = Array(rows) { DoubleArray(columns) }
    override fun get(row: Int, column: Int): Double = matrix[row][column]
    override fun set(row: Int, column: Int, value: Double) {
        matrix[row][column] = value
    }

    override fun equals(other: Any?): Boolean = this === other || other is JMatrix && deepEquals(matrix, other.matrix)

    override fun hashCode(): Int = deepHashCode(matrix)
    override fun toString(): String = "JMatrix(rows=$rows, columns=$columns, matrix=${deepToString(matrix)})"

    private companion object {
        private const val serialVersionUID = 1L
    }
}