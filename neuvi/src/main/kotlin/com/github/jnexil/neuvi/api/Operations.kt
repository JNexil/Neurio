package com.github.jnexil.neuvi.api

import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.Vector
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.api.train.*
import com.github.jnexil.neuvi.internal.*
import com.github.jnexil.neuvi.util.*
import mu.*
import java.util.*

fun MutableLayer.absorb() = Absorbent.absorb(this)
fun MutableLayer.absorbRecursive() = Absorbent.absorbRecursive(this)

fun MutableLayer.enter(vararg values: Double) = enter(MemoryProvider.viewVector(values))
fun MutableLayer.enter(values: Vector) = enter(values, DEF_INPUT)
fun MutableLayer.enter(values: Vector, default: Double) {
    logger.info {
        "Layer(size=$size).enter(default=$default, values=$values)"
    }
    val activatedDefault = activation.activate(default)
    repeat(this.values.size) {
        this.values[it] = when {
            it < values.size -> {
                val value = values[it]
                val activated = activation.activate(value)
                logger.debug { "enter unactivated($value) activated($activated) to $it neuron" }
                activated
            }
            else             -> {
                logger.debug { "enter unactivated($default) activated($activatedDefault) to $it neuron" }
                activatedDefault
            }
        }
    }
}

fun MutableLayer.activate() = values.mutate {
    activation.activate(it)
}

private const val DEF_INPUT = 1.0
private val logger = KotlinLogging.logger("MutableLayerOperations")

infix fun MutableLayer.backPropagation(training: Training): Propagation = BackPropagation(
        output = this,
        training = training,
        experience = HashMap())

fun MutableLayer.backPropagation(): Propagation = backPropagation(Training.DEFAULT)