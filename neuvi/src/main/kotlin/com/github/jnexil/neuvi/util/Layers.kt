package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.Activation.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.networks.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.api.providers.LayerProvider.Companion.layer

object Layers {
    fun linear(size: Int) = layer(size, Linear)
    fun sigmoid(size: Int) = layer(size, Sigmoid)

    fun linear() = layer(1, Linear)
    fun sigmoid() = layer(1, Linear)

    fun network(vararg layers: FlexibleLayer): Network {
        require(layers.size < 2) { "Network should contain not less than 2 layes" }
        val input = layers.first()
        val output = layers.last()
        layers.reduce { first, second ->
            first.attach(RIGHT, second)
            second
        }
        return Network.FromMutable[MemoryProvider, input, output]
    }
}