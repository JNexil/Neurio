package com.github.jnexil.neuvi.api

import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.providers.*

data class Training(val direction: Direction = LEFT,
                    val learningRate: Double = 0.5,
                    val default: Double = 1.0,
                    val momentum: Double = 1.0,
                    val memory: MemoryProvider = MemoryProvider) {
    companion object {
        val DEFAULT = Training()
    }
}