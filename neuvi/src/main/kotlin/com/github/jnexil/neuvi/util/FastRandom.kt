package com.github.jnexil.neuvi.util

import java.util.concurrent.*

object FastRandom {
    private val random = ThreadLocalRandom.current()

    val boolean: Boolean get() = random.nextBoolean()
    fun chance(chance: Float): Boolean = when {
        chance >= 1F -> true
        chance <= 0F -> false
        else         -> chance >= float
    }

    val double: Double get() = random.nextDouble()
    fun double(bound: Double): Double = random.nextDouble(bound)
    fun double(origin: Double, bound: Double): Double = random.nextDouble(origin, bound)

    val float: Float get() = random.nextFloat()
    fun float(bound: Float): Float = float(0.0F, bound)
    fun float(min: Float, max: Float): Float = float * (max - min) + min
}