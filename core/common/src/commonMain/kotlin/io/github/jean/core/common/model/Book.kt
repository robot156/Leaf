package io.github.jean.core.common.model

import kotlin.time.Instant

data class Book(
    val itemId: Long,
    val title: String,
    val link: String,
    val author: String,
    val cover: String,
    val description: String,
    val isbn13: String,
    val publisher: String,
    val createdAt: Instant,
)
