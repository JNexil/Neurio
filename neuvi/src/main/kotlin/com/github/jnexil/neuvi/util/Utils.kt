package com.github.jnexil.neuvi.util

import mu.*
import kotlin.reflect.*

infix fun <T> T.finally(action: (T) -> Unit) = apply(action)
inline fun <reified T: Any> logger(kClass: KClass<T> = T::class) = KotlinLogging.logger(kClass.java.name)