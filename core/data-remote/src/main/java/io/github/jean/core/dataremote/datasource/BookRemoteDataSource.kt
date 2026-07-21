package io.github.jean.core.dataremote.datasource

import io.github.jean.core.dataremote.model.BookContainerResponse

interface BookRemoteDataSource {
    suspend fun getSearchBooks(
        query: String,
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): BookContainerResponse

    suspend fun getBook(itemId: Long): BookContainerResponse

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 10
    }
}
