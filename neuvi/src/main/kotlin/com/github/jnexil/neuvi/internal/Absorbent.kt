package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.webs.*
import com.github.jnexil.neuvi.util.*
import com.github.jnexil.neuvi.util.Cyclic.sum

internal object Absorbent {
    val log = logger<Absorbent>()
    fun absorb(layer: MutableLayer): Boolean {
        log.info { "Absorbing single layer $layer" }
        val web = layer.left
        when (web) {
            null -> {
                log.warn { "Missing left layer" }
                return false
            }
            else -> {
                log.info { "Absorbing web of single layer $web" }
                web.absorb()
                return true
            }
        }
    }

    private fun MutableWeb.absorb() = right.values.replace { index ->
        absorb(index) finally { result ->
            log.trace { "Absorbed $index receiver with $result" }
        }
    }

    private fun MutableWeb.absorb(inbound: Int): Double {
        val result = extract(inbound)
        return right.activation.activate(result)
    }

    private fun MutableWeb.extract(inbound: Int) = sum(left.size) { index ->
        left.values[index] * get(index, inbound)
    }

    fun absorbRecursive(layer: MutableLayer): MutableLayer = layer.findLastLayer(LEFT)
                                                                     .walkThrough(RIGHT)
                                                                     .peek { it.absorb() }
                                                                     .fromLast(MutableWeb::left) ?: layer
}

