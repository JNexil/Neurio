package com.github.jnexil.neurio.charts

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.Activation.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.api.train.*
import com.github.jnexil.neuvi.util.Layers.network
import org.knowm.xchart.*

class Basic(val trainSets: Array<TrainSet>, val epochs: Int, vararg layers: Int, val training: Training = Training.DEFAULT, val activation: Activation = Tanh) {
    val charts = charts(trainSets, epochs)
    val network = network(*layers.map { LayerProvider.layer(it, activation) }.toTypedArray())
    val propagation = run {
        val out = network.output as FlexibleLayer
        out.backPropagation(training)
    }

    fun train(): GuiChart<XYChart> {
        repeat(epochs) {
            for (set in trainSets) {
                network.process(set.input)
                charts[set]!![it] = ErrorCalculator.MSE.withTrain(propagation, expected = set.output)
            }
        }
        return chart(charts).gui()
    }

    fun input(vararg values: Double): Vector = network process vec(*values)
    fun requestBoolean(): Boolean {
        val result = request()
        return result[0] > 0.5
    }

    fun request(): Vector {
        val input = readLine().orEmpty().split(delimiters = ' ').map(String::toDouble).toDoubleArray()
        val result = input(*input)
        return result
    }
}


fun vec(vararg doubles: Double) = MemoryProvider.viewVector(doubles)
fun vec(vararg ints: Int) = MemoryProvider.viewVector(ints.map(Int::toDouble).toDoubleArray())