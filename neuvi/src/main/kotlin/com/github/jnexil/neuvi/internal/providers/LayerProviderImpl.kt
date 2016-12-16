package com.github.jnexil.neuvi.internal.providers

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.internal.*

internal class LayerProviderImpl(private val memoryManagement: MemoryManagement): LayerProvider {
    override fun with(memoryManagement: MemoryManagement): LayerProvider = when (memoryManagement) {
        this.memoryManagement -> this
        else                  -> LayerProviderImpl(memoryManagement)
    }

    override fun layer(size: Int, activation: Activation) = LayerImpl(size, activation)
}
