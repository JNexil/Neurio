package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.util.*

class TrainedModel(val model: Model, training: Training) {
    val webs = model.webs.map { training.memory.matrix(it.rows, it.columns) }
    val learningRate = training.learningRate
    val target = model.expected
    val activation = model.activation

    val derivatives = model.layers.map {
        training.memory.map(from = it) { activation.derivative(it) }
    }

    val errors = model.layers.map {
        training.memory.vector(it.size)
    }

    val deltas = model.layers.map {
        training.memory.vector(it.size)
    }

    val OUTPUT = 2
    val FIRST_OUTPUT = 0
    val HIDDEN = 1
    val INPUT = 0

    init {
        errors[OUTPUT][FIRST_OUTPUT] = target - model.layers[OUTPUT][FIRST_OUTPUT]
        deltas[OUTPUT][FIRST_OUTPUT] = derivatives[OUTPUT][FIRST_OUTPUT] * errors[OUTPUT][FIRST_OUTPUT]

        repeat(2) {
            webs[HIDDEN][it, FIRST_OUTPUT] = learningRate * model.layers[HIDDEN][it] * deltas[OUTPUT][FIRST_OUTPUT]

            errors[HIDDEN][it] = deltas[OUTPUT][FIRST_OUTPUT] * model.webs[HIDDEN][it, FIRST_OUTPUT]
            println("deltas[OUTPUT][FIRST_OUTPUT] = ${deltas[OUTPUT][FIRST_OUTPUT]}")
            println("model.webs[HIDDEN][it, FIRST_OUTPUT] = ${model.webs[HIDDEN][it, FIRST_OUTPUT]}")
            println("errors[HIDDEN][it] = ${errors[HIDDEN][it]}")
            deltas[HIDDEN][it] = derivatives[HIDDEN][it] * errors[HIDDEN][it]
        }

        repeat(2) { input ->
            repeat(2) { hidden ->
                webs[INPUT][input, hidden] = learningRate * deltas[HIDDEN][hidden] * model.layers[INPUT][input]
            }
        }
    }

    override fun toString(): String = """TrainedModel(
webs=$webs,
learningRate=$learningRate,
target=$target,
activation=$activation,
derivatives=$derivatives,
errors=$errors,
deltas=$deltas,
model=$model)"""


}