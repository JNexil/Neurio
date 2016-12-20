package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.networks.*
import com.github.jnexil.neuvi.api.providers.*

internal class IONetwork internal constructor(override val input: InputLayer, override val output: OutputLayer, override val memory: MemoryProvider): Network {
    override fun process(data: Vector): Vector {
        input.enter(data)
        output.absorb()
        return memory.copyVector(output.values)
    }

    override fun train(expected: Vector) {
        throw UnsupportedOperationException("Cannot train network with immutable structure")
    }
}