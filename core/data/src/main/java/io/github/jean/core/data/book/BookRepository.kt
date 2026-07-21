package io.github.jean.core.data.book

import io.github.jean.core.common.model.Book
import io.github.jean.core.common.model.BookContainer

interface BookRepository {
    suspend fun getSearchBooks(
        query: String,
        page: Int,
        pageSize: Int,
    ): BookContainer

    suspend fun getBook(itemId: Long): Book
}
