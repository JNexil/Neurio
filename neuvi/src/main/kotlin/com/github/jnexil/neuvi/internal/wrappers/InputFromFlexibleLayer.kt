package com.github.jnexil.neuvi.internal.wrappers

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*

internal class InputFromFlexibleLayer(private val delegate: FlexibleLayer): InputLayer, FlexibleLayer by delegate {
    override fun enter(values: Vector) {
        require(values.size <= delegate.values.size) { "Redundant data of input values" }
        repeat(values.size) {
            delegate.values[it] = values[it]
        }
    }

    override fun attach(direction: Direction, layer: FlexibleLayer) = when (direction) {
        LEFT -> throw UnsupportedOperationException("Cannot attach a left layer to input layer")
        else -> delegate.attach(RIGHT, layer)
    }
}

