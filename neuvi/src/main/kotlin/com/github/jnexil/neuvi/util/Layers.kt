package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.Activation.*
import com.github.jnexil.neuvi.api.providers.LayerProvider.Companion.layer

object Layers {
    fun linear(size: Int) = layer(size, Linear)
    fun sigmoid(size: Int) = layer(size, Sigmoid)

    fun linear() = layer(1, Linear)
    fun sigmoid() = layer(1, Linear)
}