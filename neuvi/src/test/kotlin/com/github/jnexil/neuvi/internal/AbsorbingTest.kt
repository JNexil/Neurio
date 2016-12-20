package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Activation.*
import com.github.jnexil.neuvi.internal.BackPropagationTest.*
import com.github.jnexil.skribe.*
import com.github.jnexil.skribe.expect.*
import com.github.jnexil.skribe.expect.extensions.*
import com.github.jnexil.skribe.util.*

class AbsorbingTest: Skribe("Absorbing") {
    init {
        describe("When absorbing network 2:2:1")
                .intermediate
                .each(Linear, Sigmoid, Tanh)
                .move("Given network", ::Image)
                .stepwise {
                    stepR("fill weights") {
                        template.initTo(output)
                    }
                    stepR("absorb data") {
                        input.enter(template.input)
                        output.absorbRecursive()
                    }

                    testR("should update input layer") {
                        input.values.should.equal(template.layers[0])
                    }

                    testR("should update hidden layer") {
                        hidden.values.should.equal(template.layers[1])
                    }

                    testR("should update output layer") {
                        output.values.should.equal(template.layers[2])
                    }
                }
    }
}