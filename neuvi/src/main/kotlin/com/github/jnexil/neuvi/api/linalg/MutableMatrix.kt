package com.github.jnexil.neuvi.api.linalg

import java.io.*

interface MutableMatrix: Matrix, Closeable {
    operator fun set(row: Int, column: Int, value: Double)
    override fun close() {
    }
}