package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.exception.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.api.webs.*
import com.github.jnexil.neuvi.internal.providers.*
import com.github.jnexil.neuvi.util.*
import mu.*

internal class LayerImpl(size: Int, override val activation: Activation, val provider: LayerProviderImpl): FlexibleLayer {
    override val values: MutableVector = MemoryProvider.vector(size)
    override var left: MutableWeb? = null
        private set
    override var right: MutableWeb? = null
        private set

    override fun detach(direction: Direction): FlexibleLayer? = through(direction) as? FlexibleLayer finally { detached ->
        when (detached) {
            null -> debug { "$this - Detach none layer at $direction" }
            else -> {
                debug { "$this - Detach $detached at $direction" }
                setWeb(direction, web = null)
                detached.detach(direction.reverse)
            }
        }
    }

    override fun attach(direction: Direction, layer: FlexibleLayer) = when (layer) {
        this                 -> attachSelf(direction, layer)
        in direction.reverse -> attachReverse(direction, layer)
        !in direction        -> attachNew(direction, layer)
        else                 -> warn { "Try attach to $this $direction attached $layer" }
    }

    private operator fun Direction.contains(layer: Layer) = has(this, layer)

    private fun attachSelf(direction: Direction, layer: FlexibleLayer) {
        warn { "Try attach $direction self $layer" }
        throw AttachLayerException("self")
    }

    private fun attachReverse(direction: Direction, layer: FlexibleLayer) {
        warn { "Try attach $direction $layer to $this when already attached to other direction" }
        throw AttachLayerException("already attached to other direction")
    }

    private fun attachNew(direction: Direction, layer: FlexibleLayer) {
        trace { "Attaching $direction $layer to $this" }
        update(direction, layer)
        layer.attach(direction.reverse, this)
        debug { "Have attached $direction $layer to $this" }
    }

    private fun update(direction: Direction, layer: FlexibleLayer) = getWeb(layer, direction).let {
        debug { "$this.$direction = $layer" }
        setWeb(direction, it)
    }

    private fun getWeb(layer: FlexibleLayer, direction: Direction): MutableWeb = findDirectWeb(layer, direction)
                                                                                 ?: findReverseWeb(layer, direction)
                                                                                 ?: newWeb(layer, direction)


    private fun findDirectWeb(layer: FlexibleLayer, direction: Direction): MutableWeb? = this[direction, layer] finally {
        debug {
            when (it) {
                null -> "$this - Cannot find direct web with $layer"
                else -> "$this - Found direct web with $layer"
            }
        }
    }

    private fun findReverseWeb(layer: FlexibleLayer, direction: Direction): MutableWeb? = layer[direction.reverse, this] finally {
        debug {
            when (it) {
                null -> "$this - Cannot find reverse web with $layer"
                else -> "$this - Found reverse web with $layer"
            }
        }
    }

    private fun newWeb(layer: FlexibleLayer, direction: Direction): MutableWeb = when (direction) {
        LEFT  -> {
            debug { "Creating web $layer-$this" }
            provider.web(layer, this)
        }
        RIGHT -> {
            debug { "Creating web $this-$layer" }
            WebImpl(this, layer)
        }
    } finally {
        info { "Created web $it" }
    }

    private fun setWeb(direction: Direction, web: MutableWeb?) {
        trace { "Set $web at $direction" }
        return when (direction) {
            LEFT  -> left = web
            RIGHT -> right = web
        }
    }

    override fun toString(): String = buildString {
        append("Layer").append(size)
        append('(')
        append("activation=").append("activation")
        append(", left=").appendSizesThrough(LEFT)
        append(", right=").appendSizesThrough(RIGHT)
        append(')')
    }

    private fun StringBuilder.appendSizesThrough(direction: Direction) {
        when (get(direction)) {
            null -> append("none")
            else -> walkThrough(direction).joinTo(buffer = this, limit = 10, separator = ":") {
                it[direction].size.toString()
            }
        }
    }

    private companion object: KLogger by logger<LayerImpl>()
}