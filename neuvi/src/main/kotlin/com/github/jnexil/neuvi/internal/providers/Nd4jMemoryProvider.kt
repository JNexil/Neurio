package com.github.jnexil.neuvi.internal.providers

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.internal.nd4j.*
import org.nd4j.linalg.factory.*

internal object Nd4jMemoryProvider: MemoryProvider {
    val isEnabled = isSuccess {
        Class.forName("org.nd4j.linalg.factory.Nd4j")
        Nd4j.create(5).cleanup()
    }

    private inline fun isSuccess(action: () -> Unit): Boolean = try {
        action()
        true
    } catch (e: Throwable) {
        false
    }

    override fun matrix(rows: Int, columns: Int): MutableMatrix = Nd4jMatrix(rows, columns)
    override fun vector(size: Int): MutableVector = Nd4jVector(size)
}