package com.github.jnexil.neuvi.api

enum class Direction {
    LEFT,
    RIGHT;

    val reverse: Direction get() = when (this) {
        LEFT  -> RIGHT
        RIGHT -> LEFT
    }
}


