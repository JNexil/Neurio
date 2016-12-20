package com.github.jnexil.neurio.charts

import com.github.jnexil.neuvi.api.networks.*
import com.github.jnexil.neuvi.api.train.ErrorCalculator.*
import com.github.jnexil.neuvi.util.*
import java.util.*

object ODD {
    @JvmStatic
    fun main(args: Array<String>) {
        val charts: MutableMap<TrainSet, DoubleArray> = HashMap()
        val network = startODD(1000) { epoch, set, error ->
            val chart = charts.getOrPut(set) { DoubleArray(1000) }
            chart[epoch] = error
        }

        chart(trainSets = charts).gui()

        while (true) {
            println(network.request())
            println(network.requestToBoolean())
        }
    }

    private fun startODD(epochs: Int, callback: (Int, TrainSet, Double) -> Unit): Network {
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

        val network = Layers.network(Layers.tanh(5), Layers.tanh(2), Layers.tanh(1))
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