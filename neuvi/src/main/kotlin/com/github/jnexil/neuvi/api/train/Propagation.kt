package com.github.jnexil.neuvi.api.train

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import java.io.*

interface Propagation: Serializable {
    val learningRate: Double
    val output: MutableLayer
    val sender: Direction
    infix fun train(expected: Vector)
}