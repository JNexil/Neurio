package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.webs.*

operator fun Web.get(direction: Direction): Layer = when (direction) {
    LEFT  -> left
    RIGHT -> right
}

operator fun MutableWeb.get(direction: Direction): MutableLayer = when (direction) {
    LEFT  -> left
    RIGHT -> right
}

infix fun Web.through(direction: Direction): Web? {
    val layer = get(direction)
    val web = layer[direction]
    return web
}

infix fun MutableWeb.through(direction: Direction): MutableWeb? {
    val layer = get(direction)
    val web = layer[direction]
    return web
}

infix fun Web?.hasRight(layer: Layer) = this != null && layer == right
infix fun Web?.hasLeft(layer: Layer) = this != null && layer == left