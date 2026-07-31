package io.github.jean.core.common.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <T, R> Iterable<T>.mapAsync(block: suspend (T) -> R): List<R> =
    coroutineScope {
        map { async { block(it) } }.awaitAll()
    }

suspend fun <T, R> Iterable<T>.mapSafeAsync(block: suspend (T) -> R): List<R> =
    mapAsync { runCatching { block(it) }.getOrNull() }.filterNotNull()
