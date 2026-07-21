package io.github.jean.core.common.model

data class BookContainer(
    val totalResults: Int = 0,
    val startIndex: Int = 0,
    val itemsPerPage: Int = 0,
    val query: String = "",
    val books: List<Book> = emptyList(),
)
