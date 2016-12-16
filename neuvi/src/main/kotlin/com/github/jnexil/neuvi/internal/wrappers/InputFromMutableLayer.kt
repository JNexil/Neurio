package com.github.jnexil.neuvi.internal.wrappers

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*

internal class InputFromMutableLayer(private val delegate: MutableLayer): InputLayer, MutableLayer by delegate {
    override fun enter(values: Vector) = delegate.enter(values)
}

