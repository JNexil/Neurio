package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.internal.wrappers.*


fun MutableLayer.toInputLayer(): InputLayer = InputFromMutableLayer(this)
fun MutableLayer.toOutputLayer(): OutputLayer = OutputFromMutableLayer(this)

fun FlexibleLayer.toInputLayer(): InputLayer = InputFromFlexibleLayer(this)
fun FlexibleLayer.toOutputLayer(): OutputLayer = OutputFromFlexibleLayer(this)

