package com.github.jnexil.neuvi.internal.linalg

import com.github.jnexil.neuvi.api.linalg.*
import java.io.*
import java.util.Arrays.*

internal class JVector(override val size: Int): MutableVector, Serializable {
    private val array = DoubleArray(size)
    override fun get(index: Int) = array[index]
    override fun set(index: Int, value: Double) {
        array[index] = value
    }

    override fun equals(other: Any?): Boolean = this === other || other is JVector && equals(array, other.array)
    override fun hashCode(): Int = hashCode(array)
    override fun toString(): String = "JVector(size=$size, array=${toString(array)})"

    private companion object {
        private const val serialVersionUID = 1L
    }
}

