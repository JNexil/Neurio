package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.webs.*


/**
 * find last layer that linked to receiver through given [direction] or receiver
 * @return receiver if layer's edge
 * @see [findLastWeb]
 */
fun Layer.findLastLayer(direction: Direction): Layer = findLastWeb(direction)?.get(direction) ?: this

/**
 * find last layer that linked to receiver through given [direction] or receiver
 * @return receiver if layer's edge
 * @see [findLastWeb]
 */
fun MutableLayer.findLastLayer(direction: Direction): MutableLayer = findLastWeb(direction)?.get(direction) ?: this

/**
 * find last web that linked to receiver through given [direction]
 * @return null if layer's edge
 */
fun Layer.findLastWeb(direction: Direction): Web? = Cyclic.findLast {
    get(direction)
}

/**
 * find last web that linked to receiver through given [direction]
 * @return null if layer's edge
 */
fun MutableLayer.findLastWeb(direction: Direction): MutableWeb? = Cyclic.findLast {
    get(direction)
}

/**
 * find all webs linked to receiver through given [direction]
 */
fun MutableLayer.walkThrough(direction: Direction): Sequence<MutableWeb> {
    val initial = get(direction) ?: return emptySequence()
    return generateSequence(initial) { it through direction }
}