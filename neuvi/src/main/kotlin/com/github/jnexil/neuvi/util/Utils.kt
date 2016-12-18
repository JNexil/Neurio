package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.linalg.*
import com.github.jnexil.neuvi.api.webs.*
import mu.*
import java.lang.Math.*
import kotlin.reflect.*

inline infix fun <T> T.finally(action: (T) -> Unit) = apply(action)
inline fun <reified T: Any> logger(kClass: KClass<T> = T::class) = KotlinLogging.logger(kClass.java.name)

fun MutableWeb.tryClose(): Boolean = canClose finally {
    if (it) close()
}

private val MutableWeb.canClose: Boolean
    get() = left[RIGHT] == null && right[LEFT] == null

fun transformationString(before: Vector, after: Vector, prefix: String = "", postfix: String = ""): String {
    val size = max(before.size, after.size)
    return buildString {
        append(prefix)
        repeat(size) {
            if (it != 0) append(", ")
            append(before.getOrNaN(it))
            append(" to ")
            append(after.getOrNaN(it))
        }
        append(postfix)
    }
}

fun Vector.getOrNaN(index: Int): Double = when {
    index < size -> get(index)
    else         -> Double.NaN
}