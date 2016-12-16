package com.github.jnexil.neuvi.internal.nd4j

import com.github.jnexil.neuvi.api.linalg.*
import org.nd4j.linalg.factory.*
import java.io.*

internal class ND4JVector(override val size: Int): MutableVector, Serializable {
    private val matrix = Nd4j.create(size)
    override fun get(index: Int) = matrix.getDouble(index)
    override fun set(index: Int, value: Double) {
        matrix.putScalar(index, value)
    }

    override fun equals(other: Any?): Boolean = this === other || other is ND4JVector && matrix == other.matrix
    override fun hashCode(): Int = matrix.hashCode()
    override fun toString(): String = "ND4JVector(rows=$rows, columns=$columns, matrix=$matrix)"

    private companion object {
        private const val serialVersionUID = 1L
    }
}
