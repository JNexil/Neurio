package com.github.jnexil.neuvi.api

interface Activation {
    fun activate(value: Double): Double
    fun derivative(value: Double): Double

    object Linear: Activation {
        override fun activate(value: Double): Double = value
        override fun derivative(value: Double): Double = 1.0

        override fun toString(): String = "Activation.Linear"
    }

    object Sigmoid: Activation {
        override fun activate(value: Double): Double = 1 / (1 + Math.exp(-value))
        override fun derivative(value: Double): Double {
            val sigmoid = activate(value)
            return sigmoid * (1 - sigmoid)
        }

        override fun toString(): String = "Activation.Sigmoid"
    }
}