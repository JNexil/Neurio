package com.github.jnexil.neuvi.api.linalg

interface Matrix {
    val rows: Int
    val columns: Int

    operator fun get(row: Int, column: Int): Double
}