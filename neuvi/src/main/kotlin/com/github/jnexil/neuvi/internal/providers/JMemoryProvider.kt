package com.github.jnexil.neuvi.internal.providers

import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.internal.linalg.*

internal object JMemoryProvider: MemoryProvider {
    override fun matrix(rows: Int, columns: Int) = JMatrix(rows, columns)
    override fun vector(size: Int) = JVector(size)
}

