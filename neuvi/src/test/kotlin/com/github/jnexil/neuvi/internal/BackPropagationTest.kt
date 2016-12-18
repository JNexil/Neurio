package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Activation.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.networks.Network.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.api.providers.LayerProvider.Companion.layer
import com.github.jnexil.skribe.*
import com.github.jnexil.skribe.expect.*
import com.github.jnexil.skribe.expect.dev.*
import com.github.jnexil.skribe.expect.extensions.*
import com.github.jnexil.skribe.util.*

class BackPropagationTest: Skribe("Training with back propagation") {
    fun vec(vararg values: Double): Vector = MemoryProvider.viewVector(values)

    init {
        root.intermediate
                .each(Linear, Sigmoid)
                .move("Given network and back propagation", ::Image)
                .stepwise {
                    stepR("fill weights") {
                        template.initTo(output)
                    }
                    stepR("absorb data") {
                        FromMutable[MemoryProvider, input, output] process vec(template.I0, template.I1)
                    }
                    stepR("train data") {
                        propagation train vec(target)
                    }

                    share("should update web's weights") {
                        fun Expect<Double>.nearest(value: Double) = closeTo(value, delta = 0.00001)
                        testR("between first hidden and output neurons") {
                            webs[1][0, 0].should.be.nearest(trained.H0_O)
                        }
                        testR("between second hidden and output neurons") {
                            webs[1][1, 0].should.be.nearest(trained.H1_O)
                        }

                        testR("between first input and first hidden neurons") {
                            webs[0][0, 0].should.be.nearest(trained.I0_H0)
                        }
                        testR("between second input and first hidden neurons") {
                            webs[0][1, 0].should.be.nearest(trained.I1_H0)
                        }
                        testR("between first input and second hidden neurons") {
                            webs[0][0, 1].should.be.nearest(trained.I0_H1)
                        }
                        testR("between second input and second hidden neurons") {
                            webs[0][1, 1].should.be.nearest(trained.I1_H1)
                        }
                    }
                }
    }

    data class Image(val activation: Activation) {
        val input: FlexibleLayer = layer(size = 2, activation = activation)
        val hidden: FlexibleLayer = layer(size = 2, activation = activation)
        val output: FlexibleLayer = layer(size = 1, activation = activation)

        init {
            input.attach(RIGHT, hidden)
            hidden.attach(RIGHT, output)
        }

        val webs = arrayOf(input.right!!, hidden.right!!)
        val propagation = output.backPropagation(learningRate = 0.1, momentum = .0)

        val template = PerceptronTestModel(0.3, 0.7)
        val absorbed = template.absorb(activation)
        val trained = absorbed.train(activation, 0.1, target = template.O)

        val target: Double get() = template.O
    }
}



