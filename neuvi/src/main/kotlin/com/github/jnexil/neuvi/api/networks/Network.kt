package com.github.jnexil.neuvi.api.networks

import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.internal.*
import com.github.jnexil.neuvi.util.*

interface Network {
    val input: Layer
    val output: Layer
    val memory: MemoryProvider

    infix fun process(data: Vector): Vector

    object FromIO {
        operator fun get(memoryProvider: MemoryProvider, layer: Layer): Network {
            val input = layer.findLastLayer(LEFT)
            val output = layer.findLastLayer(RIGHT)
            return get(memoryProvider, input, output)
        }

        operator fun get(memoryProvider: MemoryProvider, input: Layer, output: Layer): Network {
            Require.requireIO(input, output)

            val inputLayer = input as InputLayer
            val outputLayer = output as OutputLayer
            return get(memoryProvider, inputLayer, outputLayer)
        }

        operator fun get(memoryProvider: MemoryProvider, inputLayer: InputLayer, outputLayer: OutputLayer): Network {
            return IONetwork(input = inputLayer, output = outputLayer, memory = memoryProvider)
        }
    }

    object FromMutable {
        operator fun get(memoryProvider: MemoryProvider, layer: MutableLayer): Network {
            val input = layer.findLastLayer(LEFT)
            val output = layer.findLastLayer(RIGHT)
            return get(memoryProvider, input, output)
        }

        operator fun get(memoryProvider: MemoryProvider, input: MutableLayer, output: MutableLayer): Network {
            Require.requireIOStructure(input, output)
            return MutNetwork(input, output, memoryProvider)
        }
    }
}