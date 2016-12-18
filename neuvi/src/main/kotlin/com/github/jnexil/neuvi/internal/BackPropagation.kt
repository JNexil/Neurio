package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.*
import com.github.jnexil.neuvi.api.train.*
import com.github.jnexil.neuvi.api.webs.*
import com.github.jnexil.neuvi.util.*
import mu.*

class BackPropagation(override val output: MutableLayer,
                      override val learningRate: Double,
                      override val sender: Direction,
                      val default: Double,
                      val experience: MutableMap<Web, MutableMatrix>,
                      val momentum: Double,
                      val memory: MemoryProvider): Propagation {
    val logger = KotlinLogging.logger("com.github.jnexil.neuvi.BackPropagation#${hashCode()}")
    override fun train(expected: Vector) {
        logger.info { "Start training $output with $expected" }
        val out = output.senderWeb ?: error("Output has not something layer at $sender")
        val deltas = out.trainReceiver(expected)
        val input = out.trainSenders(deltas)
        logger.debug { "Finished training $out where ${transformationString(expected, input.receiverLayer.values)}" }
    }

    private fun MutableWeb.trainReceiver(expected: Vector) = trainWithDelta { index, value ->
        logger.trace { "Train receiver value=$value, expected=${expected[index]}" }
        when {
            index >= expected.size -> default - value
            else                   -> expected[index] - value
        }
    }


    private fun MutableWeb.trainSenders(inter: Vector): MutableWeb {
        var intermediate = inter
        var next: MutableWeb = this
        while (true) {
            next = next.senderWeb ?: return next
            intermediate = next.trainSender(intermediate)
            logger.trace { "Trained sender with deltas $intermediate" }
        }
    }

    private fun MutableWeb.trainSender(inter: Vector) = trainWithDelta { sender, value ->
        logger.trace { "Training sender with inter=$inter" }
        inter.sumIndexed { receiver, delta ->
            logger.trace { "Training sender[$sender,$receiver] with delta=$delta and old weights=${this[sender, receiver]}" }
            delta * this[sender, receiver]
        }
    }

    private inline fun MutableWeb.trainWithDelta(error: (Int, Double) -> Double): Vector {
        val delta = receiverLayer.delta(error)
        logger.trace {
            transformationString(prefix = "Calculated values-deltas: ",
                                 before = receiverLayer.values,
                                 after = delta)
        }
        trainWeights(delta)
        return delta
    }

    private inline fun Layer.delta(getError: (Int, Double) -> Double) = deltas(size) {
        val received = values[it]
        val derivative = activation.derivative(received)
        val error = getError(it, received)
        logger.trace { "Delta(receiver=$received, derivative=$derivative, error=$error)" }
        derivative * error
    }

    private inline fun deltas(size: Int, initializer: (Int) -> Double) = memory.vector(size).apply {
        repeat(size) {
            this[it] = initializer(it)
        }
    }

    private fun MutableWeb.trainWeights(receiverDelta: Vector) {
        repeat(columns) { receiver ->
            val delta = receiverDelta[receiver]
            repeat(rows) { sender ->
                val experience = momentum * webExperience[sender, receiver]
                val progress = delta * left.values[sender] * learningRate
                logger.debug { "Trained experience=$experience, progress=$progress[delta=$delta]" }
                this[sender, receiver] = experience + progress
            }
        }
    }

    private val MutableWeb.webExperience: MutableMatrix
        get() = experience.getOrPut(this) { memory.matrix(rows, columns) }

    val MutableWeb.senderWeb: MutableWeb? get() = through(sender)
    val MutableLayer.senderWeb: MutableWeb? get() = get(sender)
    val MutableWeb.receiverLayer: MutableLayer get() = get(sender.reverse)
}