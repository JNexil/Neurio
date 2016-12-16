package com.github.jnexil.neuvi.internal.nd4j

import com.github.jnexil.neuvi.api.linalg.*
import org.nd4j.linalg.factory.*
import java.io.*

internal class Nd4jVector(override val size: Int): MutableVector, Serializable {
    private val matrix = Nd4j.create(size)
    override fun get(index: Int) = matrix.getDouble(index)
    override fun set(index: Int, value: Double) {
        matrix.putScalar(index, value)
    }

    override fun equals(other: Any?): Boolean = this === other || other is Nd4jVector && matrix == other.matrix
    override fun hashCode(): Int = matrix.hashCode()
    override fun toString(): String = "Nd4jVector(rows=$rows, columns=$columns, matrix=$matrix)"

    override fun close() {
        matrix.cleanup()
    }

    private companion object {
        private const val serialVersionUID = 1L
    }
}
