package com.github.jnexil.neurio.charts

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.networks.*
import com.github.jnexil.neuvi.api.providers.*

fun Network.requestToBoolean(): Boolean {
    return request()[0] > 0.5
}

fun Network.request(): Vector {
    val input = readLine().orEmpty().split(delimiters = ' ').map(String::toDouble).toDoubleArray()
    val result = process(vec(*input))
    return result
}

fun vec(vararg doubles: Double) = MemoryProvider.viewVector(doubles)
fun vec(vararg ints: Int) = MemoryProvider.viewVector(ints.map(Int::toDouble).toDoubleArray())