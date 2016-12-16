package com.github.jnexil.neuvi.api.providers

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.internal.providers.*

interface MemoryProvider {
    fun matrix(rows: Int, columns: Int): MutableMatrix
    fun vector(size: Int): MutableVector
    fun vector(values: DoubleArray): MutableVector = vector(values.size).apply {
        repeat(size) {
            this[it] = values[it]
        }
    }

    fun copyVector(values: Vector): Vector = vector(values.size).apply {
        repeat(size) {
            this[it] = values[it]
        }
    }

    fun copyMatrix(values: Matrix): Matrix = matrix(values.rows, values.columns).apply {
        repeat(rows) { row ->
            repeat(columns) { column ->
                this[row, column] = values[row, column]
            }
        }
    }

    companion object: MemoryProvider by JMemoryProvider {
        fun viewVector(values: DoubleArray) = if (values.isNotEmpty()) object: Vector {
            override val size: Int get() = values.size
            override fun get(index: Int): Double = values[index]
        } else EMPTY_VECTOR

        val EMPTY_MATRIX = object: MutableMatrix {
            override val rows: Int get() = 0
            override val columns: Int get() = 0
            override fun get(row: Int, column: Int) = throw IndexOutOfBoundsException("$row | $column at empty matrix")
            override fun set(row: Int, column: Int, value: Double) = throw IndexOutOfBoundsException("$row | $column at empty matrix")
        }

        val EMPTY_VECTOR = object: MutableVector {
            override val size: Int get() = 0
            override fun get(index: Int) = throw IndexOutOfBoundsException("$index at empty vector")
            override fun set(index: Int, value: Double) = throw IndexOutOfBoundsException("$index at empty vector")
        }
    }
}