package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.train.*
import com.github.jnexil.neuvi.api.webs.*
import com.github.jnexil.neuvi.util.*
import mu.*

class BackPropagation(
        override val output: MutableLayer,
        override val training: Training,
        val experience: MutableMap<Web, MutableMatrix>): Propagation {

    val logger = KotlinLogging.logger("com.github.jnexil.neuvi.BackPropagation#${hashCode()}")

    override fun train(expected: Vector) {
        logger.info { "Start training $output with $expected" }
        val out = output.senderWeb ?: error("Output has not something layer at ${training.direction}")
        val deltas = out.deltaReceiver(expected)
        val input = out.trainSenders(deltas)
        logger.debug { "Finished training $out where ${transformationString(expected, input.receiverLayer.values)}" }
    }

    private fun MutableWeb.deltaReceiver(expected: Vector) = receiverLayer.delta { index, value ->
        logger.trace { "Train receiver value=$value, expected=${expected[index]}" }
        when {
            index >= expected.size -> training.default - value
            else                   -> expected[index] - value
        }
    }


    private fun MutableWeb.trainSenders(inter: Vector): MutableWeb {
        var intermediate = inter
        var next: MutableWeb = this
        while (true) {
            intermediate = next.trainWeb(intermediate)
            next = next.senderWeb ?: return next
            logger.trace { "Trained sender with deltas $intermediate" }
        }
    }

    private fun MutableWeb.trainWeb(inter: Vector) = trainWithDelta(inter) { sender, value ->
        logger.trace { "Training sender with inter=$inter" }
        inter.sumIndexed { receiver, delta ->
            val web = this
            logger.trace { "Error(sender[$sender,$receiver], delta=$delta * weight=${web[sender, receiver]}) = ${delta * web[sender, receiver]}" }
            delta * web[sender, receiver]
        }
    }

    private inline fun MutableWeb.trainWithDelta(inter: Vector, error: (Int, Double) -> Double): Vector {
        logger.debug { "Origin weights $weights" }
        val nextDelta = senderLayer.delta(error)
        logger.trace {
            transformationString(prefix = "Calculated values-deltas: ",
                                 before = receiverLayer.values,
                                 after = nextDelta)
        }
        trainWeights(inter)
        return nextDelta
    }

    private inline fun Layer.delta(getError: (Int, Double) -> Double) = training.memory.vector(size) {
        val received = values[it]
        val derivative = activation.derivative(received)
        val error = getError(it, received)
        logger.trace { "Delta(receiver=$received, derivative=$derivative, error=$error)=${derivative * error}" }
        derivative * error
    }

    private fun MutableWeb.trainWeights(receiverDelta: Vector) {
        repeat(columns) { receiver ->
            val delta = receiverDelta[receiver]
            repeat(rows) { sender ->
                val experience = training.momentum * webExperience[sender, receiver]
                val progress = delta * left.values[sender] * training.learningRate
                logger.debug { "Trained experience=$experience, progress=$progress[delta=$delta]" }
                this[sender, receiver] = experience + progress
            }
        }
    }

    private val MutableWeb.webExperience: MutableMatrix
        get() = experience.getOrPut(this) { training.memory.matrix(rows, columns) }

    val MutableWeb.senderWeb: MutableWeb? get() = through(training.direction)
    val MutableLayer.senderWeb: MutableWeb? get() = get(training.direction)
    val MutableWeb.receiverWeb: MutableWeb? get() = through(training.direction.reverse)
    val MutableWeb.receiverLayer: MutableLayer get() = get(training.direction.reverse)
    val MutableWeb.senderLayer: MutableLayer get() = get(training.direction)
}