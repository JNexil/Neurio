package com.github.jnexil.neurio.charts

import org.knowm.xchart.*
import org.knowm.xchart.internal.chartpart.*
import javax.swing.*

fun xyChart(title: String = "Neural Network Progress", x: String = "Epoch", y: String = "MSE"): XYChart = XYChartBuilder().run {
    title(title)
    xAxisTitle(x)
    yAxisTitle(y)
}.build()

fun <T: Chart<*, *>> T.gui() = GuiChart(this).apply {
    displayChart()
}

fun GuiChart<XYChart>.update(trainSet: TrainSet, epoch: Int, error: Double) {
    SwingUtilities.invokeLater {
        chart.update(trainSet, epoch, error)
        repaintChart()
    }
}

fun XYChart.update(trainSet: TrainSet, epoch: Int, error: Double) {
    val name = trainSet.toString()
    val epochX = doubleArrayOf(epoch.toDouble())
    val errorY = doubleArrayOf(error)
    when (name) {
        in seriesMap -> updateXYSeries(name, epochX, errorY, null)
        else         -> addSeries(name, epochX, errorY)
    }
}

class GuiChart<T: Chart<*, *>>(val chart: T): SwingWrapper<T>(chart)