package com.github.jnexil.neuvi.internal.wrappers

import com.github.jnexil.neuvi.api.*
import com.github.jnexil.neuvi.api.layers.*

internal class OutputFromMutableLayer(private val pack: MutableLayer): OutputLayer, MutableLayer by pack {
    override fun absorb() {
        pack.absorbRecursive()
    }
}