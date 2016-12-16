package com.github.jnexil.neuvi.api.layers

import com.github.jnexil.neuvi.api.*

interface FlexibleLayer: MutableLayer {
    fun attach(direction: Direction, layer: FlexibleLayer)
    infix fun detach(direction: Direction): MutableLayer?
}