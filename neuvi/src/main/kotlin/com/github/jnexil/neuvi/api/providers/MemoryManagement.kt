package com.github.jnexil.neuvi.api.providers

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.internal.linalg.*

interface MemoryManagement {
    fun neurons(size: Int): MutableVector
    fun weights(rows: Int, columns: Int): MutableMatrix

    object Global: MemoryManagement {
        override fun neurons(size: Int): MutableVector = MemoryProvider.vector(size)
        override fun weights(rows: Int, columns: Int): MutableMatrix = MemoryProvider.matrix(rows, columns)
        override fun toString(): String = "MemoryManagement.Global"
    }

    object ThreadLocal: MemoryManagement {
        override fun neurons(size: Int): MutableVector = ThreadLocalVector(size)
        override fun weights(rows: Int, columns: Int): MutableMatrix = MemoryProvider.matrix(rows, columns)
        override fun toString(): String = "MemoryManagement.ThreadLocal"
    }
}