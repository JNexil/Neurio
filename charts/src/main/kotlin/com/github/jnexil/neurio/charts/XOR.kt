package com.github.jnexil.neurio.charts

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.providers.MemoryProvider.Companion.viewVector
import com.github.jnexil.neuvi.api.train.ErrorCalculator.*
import com.github.jnexil.neuvi.util.Layers.network
import com.github.jnexil.neuvi.util.Layers.tanh

object XOR {
    @JvmStatic
    fun main(args: Array<String>) {
        val set00 = TrainSet(input = vec(0.0, 0.0), output = vec(0.0))
        val set11 = TrainSet(input = vec(1.0, 1.0), output = vec(0.0))
        val set01 = TrainSet(input = vec(0.0, 1.0), output = vec(1.0))
        val set10 = TrainSet(input = vec(1.0, 0.0), output = vec(1.0))

        val trainSets = arrayOf(set00, set11, set01, set10)

        val chart = xyChart(title = "XOR Network").gui()

        val outLayer = tanh(1)
        val network = network(tanh(3), tanh(2), outLayer)
        val propagation = outLayer.backPropagation()

        repeat(1000) { epoch ->
            for (set in trainSets) {
                network.process(set.input)
                val error = MSE.withTrain(propagation, set.output)

                chart.update(set, epoch, error)
            }
        }
    }

    fun vec(vararg doubles: Double) = viewVector(doubles)
}

object ODD {
    @JvmStatic
    fun main(args: Array<String>) {
        val trainSets = arrayOf(TrainSet(vec(0, 0, 0, 0), vec(0)),
                                TrainSet(vec(0, 0, 0, 1), vec(1)),
                                TrainSet(vec(0, 0, 1, 0), vec(0)),
                                TrainSet(vec(0, 1, 1, 0), vec(0)),
                                TrainSet(vec(0, 1, 1, 1), vec(1)),
                                TrainSet(vec(1, 0, 1, 0), vec(0)),
                                TrainSet(vec(1, 0, 1, 1), vec(1)),
                                TrainSet(vec(1, 1, 0, 0), vec(0)),
                                TrainSet(vec(1, 1, 0, 1), vec(1)),
                                TrainSet(vec(1, 1, 1, 0), vec(0)),
                                TrainSet(vec(1, 1, 1, 1), vec(1)))

        val chart = xyChart(title = "Network").gui()

        val outLayer = tanh(1)
        val network = network(tanh(4), tanh(2), outLayer)
        val propagation = outLayer.backPropagation()

        repeat(1000) { epoch ->
            for (set in trainSets) {
                network.process(set.input)
                val error = MSE.withTrain(propagation, set.output)

                println("error = ${error}")
                chart.update(set, epoch, error)
            }
        }
    }

    fun vec(vararg ints: Int) = viewVector(ints.map(Int::toDouble).toDoubleArray())
}


