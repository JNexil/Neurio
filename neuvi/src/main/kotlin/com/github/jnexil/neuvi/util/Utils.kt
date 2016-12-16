package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.Direction.*
import com.github.jnexil.neuvi.api.webs.*
import mu.*
import kotlin.reflect.*

inline infix fun <T> T.finally(action: (T) -> Unit) = apply(action)
inline fun <reified T: Any> logger(kClass: KClass<T> = T::class) = KotlinLogging.logger(kClass.java.name)

fun MutableWeb.tryClose(): Boolean = canClose finally {
    if (it) close()
}

private val MutableWeb.canClose: Boolean
    get() = left[RIGHT] == null && right[LEFT] == null