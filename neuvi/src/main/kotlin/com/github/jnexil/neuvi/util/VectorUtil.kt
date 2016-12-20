package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.*

inline fun MemoryProvider.vector(size: Int, initializer: (Int) -> Double) = vector(size).apply {
    repeat(size) {
        this[it] = initializer(it)
    }
}

inline fun MemoryProvider.map(from: Vector, mapping: (Double) -> Double) = vector(from.size) {
    mapping(from[it])
}