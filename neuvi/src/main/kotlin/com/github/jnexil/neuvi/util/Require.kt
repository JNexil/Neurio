package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.layers.*

object Require {
    fun requireIO(input: Layer, output: Layer) {
        requireIOStructure(input, output)
        requireIOInstances(input, output)
    }

    fun requireIOInstances(input: Layer, output: Layer) {
        requireInputInstance(input)
        requireOutputInstance(output)
    }

    fun requireOutputInstance(output: Layer) {
        require(output is OutputLayer) { "Output layer should be instance of OutputLayer" }
    }

    fun requireInputInstance(input: Layer) {
        require(input is InputLayer) { "Input layer should be instance of InputLayer" }
    }

    fun requireIOStructure(input: Layer, output: Layer) {
        require(input != output) { "Network can't contains single layer" }
        requireInputStructure(input)
        requireOutputStructure(output)
    }

    fun requireOutputStructure(output: Layer) {
        require(output.right == null) { "Output layer should not contain right web" }
        require(output.left != null) { "Output layer should contain left web" }
    }

    fun requireInputStructure(input: Layer) {
        require(input.left == null) { "Input layer should not contain left web" }
        require(input.right != null) { "Input layer should contain right web" }
    }
}