package com.github.jnexil.neuvi.api.providers

import com.github.jnexil.neuvi.util.*

interface Initializer {
    fun weight(row: Int, column: Int): Double

    object Random: Initializer {
        override fun weight(row: Int, column: Int): Double = FastRandom.double(0.2, 0.8)
    }
}