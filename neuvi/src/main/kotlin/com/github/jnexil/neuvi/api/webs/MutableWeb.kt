package com.github.jnexil.neuvi.api.webs

import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*

interface MutableWeb: MutableMatrix, Web {
    override val left: MutableLayer
    override val right: MutableLayer
}