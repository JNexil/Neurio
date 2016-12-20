package com.github.jnexil.neuvi.api.webs

import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*

interface Web: Matrix {
    val weights: Matrix
    val left: Layer
    val right: Layer
}

