package com.github.jnexil.neuvi.api

import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.MemoryProvider.Companion.vector
import com.github.jnexil.neuvi.api.webs.*
import com.github.jnexil.skribe.expect.*
import com.github.jnexil.skribe.expect.dev.*
import com.github.jnexil.skribe.expect.extensions.*
import org.jetbrains.spek.api.*
import org.jetbrains.spek.api.dsl.*

class OperationsKtTest: Spek(spec = {
    class ValuesLayer(size: Int): MutableLayer {
        override val values: MutableVector = vector(size)
        override val activation: Activation = Activation.Sigmoid
        override val left: MutableWeb? get() = throw UnsupportedOperationException()
        override val right: MutableWeb? get() = throw UnsupportedOperationException()
    }

    fun Layer.shouldHaveActivated(vararg values: Double) {
        val expected = DoubleArray(values.size) { activation.activate(values[it]) }
        this.values.shouldHave(*expected)
    }
    describe("enter operation") {
        context("equivalent sizes") {
            it("should write all activated values to target") {
                val values = doubleArrayOf(3.0, 2.0, 2.5, 1.4, 3.2)
                val target = ValuesLayer(5)
                target.enter(vector(values))

                target.shouldHaveActivated(3.0, 2.0, 2.5, 1.4, 3.2)
            }
        }
        context("target has greater size") {
            it("should write all activated values to target and set other with 1.0") {
                val values = doubleArrayOf(3.0, 2.0, 2.5, 1.4, 3.2)
                val target = ValuesLayer(6)
                target.enter(vector(values))

                target.shouldHaveActivated(3.0, 2.0, 2.5, 1.4, 3.2, 1.0)
            }
        }
        context("target has less size") {
            it("should write all activated values to target, ignoring remainders") {
                val values = doubleArrayOf(3.0, 2.0, 2.5, 1.4, 3.2)
                val target = ValuesLayer(4)
                target.enter(vector(values))

                target.shouldHaveActivated(3.0, 2.0, 2.5, 1.4)
            }
        }
    }
})

fun Vector.shouldHave(vararg values: Double) {
    size.should.be.equal(values.size)
    repeat(size) {
        get(it).should.be.closeTo(values[it], 0.01)
    }
}

