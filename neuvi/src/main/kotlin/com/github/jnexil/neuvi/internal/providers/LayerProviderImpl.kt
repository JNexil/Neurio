package com.github.jnexil.neuvi.internal.providers

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.api.providers.Initializer.*
import com.github.jnexil.neuvi.api.providers.MemoryManagement.*
import com.github.jnexil.neuvi.internal.*
import com.github.jnexil.neuvi.util.*

internal class LayerProviderImpl(private val memoryManagement: MemoryManagement = Global, private val initializer: Initializer = Random): LayerProvider {
    override fun with(memory: MemoryManagement, initializer: Initializer): LayerProvider = when {
        memory == this.memoryManagement && initializer == this.initializer -> this
        else                                                               -> LayerProviderImpl(memory, initializer)
    }

    override fun layer(size: Int, activation: Activation) = LayerImpl(size, activation, this)
    fun web(left: FlexibleLayer, right: LayerImpl): WebImpl = WebImpl(left, right) finally {
        repeat(it.rows) { row ->
            repeat(it.columns) { column ->
                it[row, column] = initializer.weight(row, column)
            }
        }
    }
}
