package com.github.jnexil.neuvi.api.linalg

interface MutableMatrix: Matrix {
    operator fun set(row: Int, column: Int, value: Double)
}