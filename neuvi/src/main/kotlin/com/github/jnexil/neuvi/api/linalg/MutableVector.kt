package com.github.jnexil.neuvi.api.linalg

interface MutableVector: Vector, MutableMatrix {
    operator fun set(index: Int, value: Double)

    override fun set(row: Int, column: Int, value: Double) = when (column) {
        0    -> set(row, value)
        else -> throw IndexOutOfBoundsException("Column $column at vector")
    }
}