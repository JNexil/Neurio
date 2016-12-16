package com.github.jnexil.neuvi.api.layers

import com.github.jnexil.neuvi.api.linalg.*

interface InputLayer: Layer {
    fun enter(values: Vector)
}