package com.github.jnexil.neuvi.util

inline fun <T> Sequence<T>.peek(crossinline action: (T) -> Unit) = map {
    it.apply { action(it) }
}

inline fun <T, R> Sequence<T>.fromLast(crossinline map: (T) -> R): R? = lastOrNull()?.let(map)

fun <T: Any> Iterator<T>.nextOrNull() = when {
    hasNext() -> next()
    else      -> null
}