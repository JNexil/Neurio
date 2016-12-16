package com.github.jnexil.neuvi.internal.linalg

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.MemoryManagement.*
import su.jfdev.anci.util.*
import java.io.*

internal class ThreadLocalVector(override val size: Int): MutableVector, Serializable {
    private val threadLocal = ThreadLocal { Global.neurons(size) }
    private val vector: MutableVector get() = threadLocal.get()

    override fun set(index: Int, value: Double) = vector.set(index, value)
    override fun get(index: Int): Double = vector[index]

    override fun equals(other: Any?): Boolean = vector == other
    override fun hashCode(): Int = vector.hashCode()
    override fun toString(): String = vector.toString()

    private companion object {
        private const val serialVersionUID = 1L
    }
}