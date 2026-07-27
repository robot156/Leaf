package io.github.jean.core.dataremote.datasource

import io.github.jean.core.common.model.BookContainer

interface BookRemoteDataSource {
    suspend fun getSearchBooks(
        query: String,
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): BookContainer

    suspend fun getBook(itemId: Long): BookContainer

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 10
    }
}
