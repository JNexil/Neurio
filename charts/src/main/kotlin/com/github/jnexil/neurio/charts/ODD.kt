package com.github.jnexil.neurio.charts

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

        val basic = Basic(trainSets, epochs = 1000, layers = *intArrayOf(5, 2, 1))
        basic.train()

        while (true) {
            println(basic.requestBoolean())
        }
    }

}