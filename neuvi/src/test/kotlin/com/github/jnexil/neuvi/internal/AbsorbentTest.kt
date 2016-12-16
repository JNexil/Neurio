package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Activation.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.util.*
import com.github.jnexil.neuvi.util.Layers.linear
import com.github.jnexil.neuvi.util.Layers.sigmoid
import org.jetbrains.spek.api.*
import org.jetbrains.spek.api.dsl.*
import kotlin.test.*

class AbsorbentTest: Spek(spec = {
    describe("layer absorbing") {
        fun <T: MutableLayer> T.entering(vararg values: Double) = apply { enter(*values) }
        fun <T: MutableLayer> T.random() = apply {
            values.replace { Math.random() }
        }
        context("absorb lonely layer with 1 neuron") {
            val lonely = linear().random()
            it("should return false") {
                assertFalse { lonely.absorb() }
            }
        }
        context("absorb lonely layer with 15 neurons") {
            val lonely = linear(15).random()
            it("should return false") {
                assertFalse { lonely.absorb() }
            }
        }
        context("absorb right layer of 1x1 layers") {
            on("linear activation") {
                val left = linear().entering(0.5)
                val right = linear()
                left.attach(RIGHT, right)
                left.right!![0, 0] = 0.4

                it("should calculate values") {
                    assertTrue("should return true") {
                        right.absorb()
                    }
                    left.values.shouldHave(0.5)
                    right.values.shouldHave(0.5 * 0.4)
                }
            }
            on("sigmoid activation") {
                val left = sigmoid().entering(0.5)
                val right = sigmoid()
                left.attach(RIGHT, right)
                left.right!![0, 0] = 0.4

                it("should calculate values") {
                    assertTrue("should return true") {
                        right.absorb()
                    }
                    val sigmoidLeft = Sigmoid.activate(0.5)
                    val sigmoidRight = Sigmoid.activate(sigmoidLeft * 0.4)
                    left.values.shouldHave(sigmoidLeft)
                    right.values.shouldHave(sigmoidRight)
                }
            }
        }
    }
})