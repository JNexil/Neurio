package com.github.jnexil.neuvi.api.providers

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Activation.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.internal.providers.*

interface LayerProvider {
    fun layer(size: Int, activation: Activation = Sigmoid): FlexibleLayer
    fun with(memory: MemoryManagement = MemoryManagement.Global, initializer: Initializer = Initializer.Random): LayerProvider

    companion object: LayerProvider by LayerProviderImpl(MemoryManagement.Global)
}