package com.github.jnexil.neuvi.api.train

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import java.io.*

interface Propagation: Serializable {
    val output: MutableLayer
    val training: Training
    infix fun train(expected: Vector)
}