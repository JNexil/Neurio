package com.github.jnexil.neuvi.internal

import com.github.jnexil.neuvi.api.layers.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.providers.MemoryManagement.*
import com.github.jnexil.neuvi.api.webs.*
import com.github.jnexil.neuvi.util.*

internal class WebImpl(override val left: MutableLayer,
                       override val right: MutableLayer,
                       override val weights: MutableMatrix = Global.weights(left.size, right.size)): MutableMatrix by weights, MutableWeb {
    override fun toString(): String = "Web(left=$left, right=$right)"
}