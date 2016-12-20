package com.github.jnexil.neurio.charts

import com.github.jnexil.neuvi.api.networks.*
import com.github.jnexil.neuvi.api.train.ErrorCalculator.*
import com.github.jnexil.neuvi.util.Layers.network
import com.github.jnexil.neuvi.util.Layers.tanh
import java.util.*

object XOR {
    @JvmStatic
    fun main(args: Array<String>) {
        val charts: MutableMap<TrainSet, DoubleArray> = HashMap()
        val network = startXOR(1000) { epoch, set, error ->
            val chart = charts.getOrPut(set) { DoubleArray(1000) }
            chart[epoch] = error
        }

        chart(trainSets = charts).gui()

        while (true) {
            println(network.request())
            println(network.requestToBoolean())
        }
    }

    private fun startXOR(epochs: Int, callback: (Int, TrainSet, Double) -> Unit): Network {
        val set00 = TrainSet(input = vec(0.0, 0.0), output = vec(0.0))
        val set11 = TrainSet(input = vec(1.0, 1.0), output = vec(0.0))
        val set01 = TrainSet(input = vec(0.0, 1.0), output = vec(1.0))
        val set10 = TrainSet(input = vec(1.0, 0.0), output = vec(1.0))

        val trainSets = arrayOf(set00, set11, set01, set10)

        val network = network(tanh(3), tanh(2), tanh(1))
        repeat(epochs) { epoch ->
            for (set in trainSets) {
                network.process(set.input)
                network.train(set.output)
                val error = network.error(MSE, set.output)
                callback(epoch, set, error)
            }
        }
        return network
    }
}


