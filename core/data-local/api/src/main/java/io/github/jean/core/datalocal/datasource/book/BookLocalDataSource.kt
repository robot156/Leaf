package io.github.jean.core.datalocal.datasource.book

import io.github.jean.core.common.model.Book
import io.github.jean.core.common.model.BookContainer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

interface BookLocalDataSource {
    suspend fun saveBooks(books: List<Book>)

    suspend fun getBooks(): List<Book>

    suspend fun getBook(itemId: Long): Book?

    /**
     * (검색어, 페이지)로 캐싱된 검색 결과를 반환한다.
     * 캐시가 없거나 [ttl]보다 오래됐으면 null.
     */
    suspend fun getSearch(
        query: String,
        page: Int,
        ttl: Duration = DEFAULT_TTL,
    ): BookContainer?

    /** 검색 결과를 (검색어, 페이지) 단위로 캐싱한다. */
    suspend fun saveSearch(
        query: String,
        page: Int,
        container: BookContainer,
    )

    /** 캐싱된 검색 결과를 모두 비운다. (캐시된 책/노트는 유지) */
    suspend fun clearSearchCache()

    companion object {
        val DEFAULT_TTL: Duration = 7.days
    }
}
