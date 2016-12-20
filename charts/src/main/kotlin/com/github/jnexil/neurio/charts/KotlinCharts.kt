package com.github.jnexil.neurio.charts

import org.knowm.xchart.*
import org.knowm.xchart.internal.chartpart.*

fun charts(trainSets: Array<TrainSet>, epochs: Int) = trainSets.associate { it to DoubleArray(epochs) }
fun chart(trainSets: Map<TrainSet, DoubleArray>, title: String = "Neural Network Progress", x: String = "Epoch", y: String = "MSE"): XYChart {
    val names = trainSets.keys.map(TrainSet::toString).toTypedArray()
    val xKeys = (0..trainSets.values.first().size - 1).map(Int::toDouble).toDoubleArray()
    val yKeys = trainSets.values.toTypedArray()
    return QuickChart.getChart(title, x, y, names, xKeys, yKeys)
}

fun <T: Chart<*, *>> T.gui() = GuiChart(this).apply {
    displayChart()
}
class GuiChart<T: Chart<*, *>>(val chart: T): SwingWrapper<T>(chart)