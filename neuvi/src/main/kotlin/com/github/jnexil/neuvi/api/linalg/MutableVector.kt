package com.github.jnexil.neuvi.api.linalg

import java.io.*

interface MutableVector: Vector, MutableMatrix, Closeable {
    operator fun set(index: Int, value: Double)

    override fun set(row: Int, column: Int, value: Double) = when (column) {
        0    -> set(row, value)
        else -> throw IndexOutOfBoundsException("Column $column at vector")
    }

    override fun close() {
    }
}