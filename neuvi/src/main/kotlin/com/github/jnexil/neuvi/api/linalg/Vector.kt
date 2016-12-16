package com.github.jnexil.neuvi.api.linalg

interface Vector: Matrix {
    val size: Int

    operator fun get(index: Int): Double

    override val rows: Int get() = size
    override val columns: Int get() = 1

    override fun get(row: Int, column: Int): Double = when (column) {
        0    -> get(row)
        else -> throw IndexOutOfBoundsException("Column $column at vector")
    }

    fun toArray(): DoubleArray = DoubleArray(size) { get(it) }
}