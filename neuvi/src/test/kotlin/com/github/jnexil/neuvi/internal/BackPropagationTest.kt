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
import mu.*

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
                        FromMutable[MemoryProvider, input, output] process template.input
                    }
                    stepR("train data") {
                        propagation train vec(target)
                    }
                    step {
                        logger.info { "${it.trained}" }
                    }

                    share("should update web's weights") {
                        fun Image.shouldBe(web: Int, sender: Int, receiver: Int) {
                            webs[web][sender, receiver]
                                    .should
                                    .be
                                    .closeTo(trained.webs[web][sender, receiver], delta = 0.000000)
                        }
                        testR("between first hidden and output neurons") {
                            shouldBe(web = 1, sender = 0, receiver = 0)
                        }
                        testR("between second hidden and output neurons") {
                            shouldBe(web = 1, sender = 1, receiver = 0)
                        }
                        testR("between first input and first hidden neurons") {
                            shouldBe(web = 0, sender = 0, receiver = 0)
                        }
                        testR("between second input and first hidden neurons") {
                            shouldBe(web = 0, sender = 1, receiver = 0)
                        }
                        testR("between first input and second hidden neurons") {
                            shouldBe(web = 0, sender = 0, receiver = 1)
                        }
                        testR("between second input and second hidden neurons") {
                            shouldBe(web = 0, sender = 1, receiver = 1)
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
        val propagation = output.backPropagation()

        val template = Model(activation)
        val trained = TrainedModel(template, training = propagation.training)

        val target: Double get() = template.expected
    }

    companion object: KLogging()
}



