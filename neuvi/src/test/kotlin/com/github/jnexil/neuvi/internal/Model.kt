package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.providers.MemoryManagement.*
import com.github.jnexil.neuvi.util.*

class Model(val activation: Activation) {
    val input = Global.neurons(2).replace { FastRandom.double }
    val expected = FastRandom.double

    val layers = listOf(Global.neurons(2),
                        Global.neurons(2),
                        Global.neurons(1))

    val webs = listOf(Global.weights(2, 2),
                      Global.weights(2, 1))

    init {
        for (web in webs) {
            repeat(web.rows) { row ->
                repeat(web.columns) { column ->
                    web[row, column] = FastRandom.double
                }
            }
        }
        layers[0].let {
            it[0] = activation.activate(input[0])
            it[1] = activation.activate(input[1])
        }
        layers[1].let {
            val previous = layers[0]
            val web = webs[0]
            it[0] = activation.activate(previous[0] * web[0, 0] + previous[1] * web[1, 0])
            it[1] = activation.activate(previous[0] * web[0, 1] + previous[1] * web[1, 1])
        }
        layers[2].let {
            val previous = layers[1]
            val web = webs[1]
            it[0] = activation.activate(previous[0] * web[0, 0] + previous[1] * web[1, 0])
        }
    }

    fun initTo(output: MutableLayer, direction: Direction = LEFT) {
        output[direction]!!.finally {
            it[0, 0] = webs[1][0, 0]
            it[1, 0] = webs[1][1, 0]
        }.through(direction)!!.let {
            it[0, 0] = webs[0][0, 0]
            it[1, 0] = webs[0][1, 0]
            it[1, 1] = webs[0][1, 1]
            it[0, 1] = webs[0][0, 1]
        }
    }

    override fun toString(): String {
        return """Model(
activation=$activation,
input=$input,
expected=$expected,
layers=$layers,
webs=$webs)"""
    }


}