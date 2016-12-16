@file:Suppress("LoopToCallChain")

package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.linalg.*

inline fun MutableVector.mutate(action: (Double) -> Double): MutableVector = replace {
    action(get(it))
}

inline fun MutableVector.replace(action: (Int) -> Double) = apply {
    repeat(size) {
        set(it, action(it))
    }
}

inline fun Vector.sumIndexed(mapping: (Int, Double) -> Double): Double = Cyclic.sum(size) {
    mapping(it, get(it))
}

inline fun DoubleArray.sumIndexed(mapping: (Int, Double) -> Double): Double = Cyclic.sum(size) {
    mapping(it, get(it))
}


object Cyclic {
    inline fun sum(size: Int, mapping: (Int) -> Double): Double {
        var sum = .0
        for (i in 0..size - 1) {
            sum += mapping(i)
        }
        return sum
    }

    inline fun <R> findLast(next: () -> R?): R? {
        var last: R? = null
        while (true) {
            last = next() ?: return last
        }
    }

    inline fun <R> findLast(seed: R, next: (R) -> R?): R {
        var last: R = seed
        while (true) {
            last = next(last) ?: return last
        }
    }
}