package com.github.jnexil.neurio.charts

object XOR {
    @JvmStatic
    fun main(args: Array<String>) {
        val set00 = TrainSet(input = vec(0.0, 0.0), output = vec(0.0))
        val set11 = TrainSet(input = vec(1.0, 1.0), output = vec(0.0))
        val set01 = TrainSet(input = vec(0.0, 1.0), output = vec(1.0))
        val set10 = TrainSet(input = vec(1.0, 0.0), output = vec(1.0))

        val trainSets = arrayOf(set00, set11, set01, set10)

        val basic = Basic(trainSets, epochs = 1000, layers = *intArrayOf(3, 2, 1))
        basic.train()

        while (true) {
            println(basic.requestBoolean())
        }
    }
}


