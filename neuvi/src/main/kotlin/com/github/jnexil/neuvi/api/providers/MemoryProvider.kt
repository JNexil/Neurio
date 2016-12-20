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

    companion object: MemoryProvider {
        val provider = when {
            Nd4jMemoryProvider.isEnabled -> Nd4jMemoryProvider
            else                         -> JMemoryProvider
        }

        override fun matrix(rows: Int, columns: Int): MutableMatrix = provider.matrix(rows, columns)
        override fun vector(size: Int): MutableVector = provider.vector(size)

        fun viewVector(values: DoubleArray) = if (values.isNotEmpty()) object: Vector {
            override val size: Int get() = values.size
            override fun get(index: Int): Double = values[index]
            override fun toString(): String = values.joinToString(prefix = "[", postfix = "]")
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