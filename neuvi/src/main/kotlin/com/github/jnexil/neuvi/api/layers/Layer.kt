package com.github.jnexil.neuvi.api.layers

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.webs.*

interface Layer {
    val activation: Activation
    val values: Vector
    val left: Web?
    val right: Web?
}