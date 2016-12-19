package com.github.jnexil.neurio.charts

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.*

data class TrainSet(val input: Vector, val output: Vector) {
    constructor(input: DoubleArray, output: DoubleArray): this(MemoryProvider.viewVector(input), MemoryProvider.viewVector(output))
}