@file:Suppress("NOTHING_TO_INLINE")

package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.webs.*

infix fun Layer.through(direction: Direction): Layer? {
    val web = get(direction) ?: return null
    return web[direction]
}

infix fun MutableLayer.through(direction: Direction): MutableLayer? {
    val web = get(direction) ?: return null
    return web[direction]
}

operator fun Layer.get(direction: Direction): Web? = when (direction) {
    LEFT  -> left
    RIGHT -> right
    else  -> null
}

operator fun MutableLayer.get(direction: Direction): MutableWeb? = when (direction) {
    LEFT  -> left
    RIGHT -> right
    else  -> null
}

operator fun Layer.get(direction: Direction, layer: Layer): Web? = when {
    has(direction, layer) -> get(direction)
    else                  -> null
}

operator fun MutableLayer.get(direction: Direction, layer: Layer): MutableWeb? = when {
    has(direction, layer) -> get(direction)
    else                  -> null
}

fun Layer.has(direction: Direction, layer: Layer): Boolean = when (direction) {
    LEFT  -> hasLeft(layer)
    RIGHT -> hasRight(layer)
}

inline fun Layer.hasLeft(layer: Layer) = left.hasLeft(layer)
inline fun Layer.hasRight(layer: Layer) = right.hasRight(layer)
val Layer.size: Int get() = values.size