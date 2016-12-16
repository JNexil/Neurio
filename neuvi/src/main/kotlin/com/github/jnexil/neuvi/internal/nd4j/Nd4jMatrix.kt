package com.github.jnexil.neuvi.internal.nd4j

import com.github.jnexil.neuvi.api.linalg.*
import org.nd4j.linalg.api.ndarray.*
import org.nd4j.linalg.factory.*
import java.io.*

internal class ND4JMatrix(override val rows: Int, override val columns: Int): MutableMatrix, Serializable {
    private val matrix: INDArray = Nd4j.create(rows, columns)
    override fun get(row: Int, column: Int): Double = matrix.getDouble(row, column)
    override fun set(row: Int, column: Int, value: Double) {
        matrix.putScalar(row, column, value)
    }

    override fun equals(other: Any?): Boolean = this === other || other is ND4JMatrix && matrix == other.matrix
    override fun hashCode(): Int = matrix.hashCode()
    override fun toString(): String = "ND4JMatrix(rows=$rows, columns=$columns, matrix=$matrix)"

    private companion object {
        private const val serialVersionUID = 1L
    }
}