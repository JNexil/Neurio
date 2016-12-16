package com.github.jnexil.neuvi.internal.wrappers

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*

internal class OutputFromFlexibleLayer(private val pack: FlexibleLayer): OutputLayer, FlexibleLayer by pack {
    override fun absorb() {
        pack.absorbRecursive()
    }

    override fun attach(direction: Direction, layer: FlexibleLayer) = when (direction) {
        RIGHT -> throw UnsupportedOperationException("Cannot attach a right layer to output layer")
        else  -> pack.attach(LEFT, layer)
    }
}

