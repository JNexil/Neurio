package com.github.jnexil.neuvi.api.train

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.util.*

enum class ErrorCalculator {
    MSE {
        override fun calculate(actual: Vector, expected: Vector): Double {
            val sum = expected.sumIndexed { i, value ->
                val delta = actual[i] - value
                delta * delta
            }
            return sum / expected.size
        }
    },
    ROOT_MSE {
        override fun calculate(actual: Vector, expected: Vector): Double {
            return Math.sqrt(ROOT_MSE.calculate(actual, expected))
        }
    };

    abstract fun calculate(actual: Vector, expected: Vector): Double

    fun withTrain(propagation: Propagation, expected: Vector): Double {
        propagation train expected
        return calculate(propagation.output.values, expected)
    }
}