package com.github.jnexil.neuvi.api.layers

import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.webs.*

interface MutableLayer: Layer {
    override val values: MutableVector
    override val left: MutableWeb?
    override val right: MutableWeb?
}