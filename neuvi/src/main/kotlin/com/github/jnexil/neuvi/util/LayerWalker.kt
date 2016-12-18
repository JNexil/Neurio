package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.webs.*


/**
 * find last layer that linked to receiver through given [direction] or receiver
 * @return receiver if layer's edge
 */
fun Layer.findLastLayer(direction: Direction): Layer = Cyclic.findLast(this) {
    it through direction
}

/**
 * find last layer that linked to receiver through given [direction] or receiver
 * @return receiver if layer's edge
 */
fun MutableLayer.findLastLayer(direction: Direction): MutableLayer = Cyclic.findLast(this) {
    it through direction
}

/**
 * find all webs linked to receiver through given [direction]
 */
fun MutableLayer.walkThrough(direction: Direction): Sequence<MutableWeb> {
    val initial = get(direction) ?: return emptySequence()
    return generateSequence(initial) { it through direction }
}