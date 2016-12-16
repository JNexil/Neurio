package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.networks.*
import com.github.jnexil.neuvi.api.providers.*

internal class MutNetwork internal constructor(override val input: MutableLayer, override val output: MutableLayer, override val memory: MemoryProvider): Network {
    override fun process(data: Vector): Vector {
        input.enter(data)
        output.absorbRecursive()
        return memory.copyVector(output.values)
    }
}