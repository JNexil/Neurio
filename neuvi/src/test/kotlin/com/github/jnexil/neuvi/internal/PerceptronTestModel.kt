package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.util.*

data class PerceptronTestModel(val I0: Double, val I0_H0: Double, val I0_H1: Double,
                               val I1: Double, val I1_H0: Double, val I1_H1: Double,
                               val H0: Double, val H0_O: Double,
                               val H1: Double, val H1_O: Double,
                               val O: Double) {

    constructor(min: Double, max: Double): this(
            FastRandom.double(min, max), FastRandom.double(min, max), FastRandom.double(min, max),
            FastRandom.double(min, max), FastRandom.double(min, max), FastRandom.double(min, max),
            FastRandom.double(min, max), FastRandom.double(min, max),
            FastRandom.double(min, max), FastRandom.double(min, max),
            FastRandom.double(min, max))

    fun absorb(activation: Activation): PerceptronTestModel {
        val actI0 = activation.activate(I0)
        val actI1 = activation.activate(I1)
        val actH0 = activation.activate(actI0 * I0_H0 + actI1 * I1_H0)
        val actH1 = activation.activate(actI0 * I0_H1 + actI1 * I1_H1)
        val actO = activation.activate(actH0 * H0_O + actH1 * H1_O)
        return copy(I0 = actI0, I1 = actI1, H0 = actH0, H1 = actH1, O = actO)
    }

    fun train(activation: Activation, learningRate: Double, target: Double): PerceptronTestModel {
        fun deltas(error: Double, out: Double) = activation.derivative(out) * error
        fun weights(delta: Double, out: Double) = learningRate * delta * out
        val ODelta = deltas(out = this.O, error = target - this.O)
        val H0_O = weights(out = this.H0, delta = ODelta)
        val H1_O = weights(out = this.H1, delta = ODelta)
        val H0Delta = deltas(out = this.H0, error = ODelta * this.H0_O)
        val H1Delta = deltas(out = this.H1, error = ODelta * this.H1_O)
        val I0_H0 = weights(out = this.I0, delta = H0Delta)
        val I0_H1 = weights(out = this.I0, delta = H1Delta)
        val I1_H0 = weights(out = this.I1, delta = H0Delta)
        val I1_H1 = weights(out = this.I1, delta = H1Delta)
        return copy(H0_O = H0_O, I0_H0 = I0_H0, I1_H0 = I1_H0, H1_O = H1_O, I0_H1 = I0_H1, I1_H1 = I1_H1)
    }


    fun initTo(output: MutableLayer, direction: Direction = LEFT) {
        output[direction]!!.finally {
            it[0, 0] = H0_O
            it[1, 0] = H1_O
        }.through(direction)!!.let {
            it[0, 0] = I0_H0
            it[0, 1] = I0_H1
            it[1, 0] = I1_H0
            it[1, 1] = I1_H1
        }
    }
}