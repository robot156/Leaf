package io.github.jean.core.data.book

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.model.Book
import io.github.jean.core.common.model.BookContainer
import io.github.jean.core.datalocal.datasource.book.BookLocalDataSource
import io.github.jean.core.dataremote.datasource.BookRemoteDataSource
import io.github.jean.core.dataremote.model.toDomain

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class BookRepositoryImpl(
    private val bookRemoteDataSource: BookRemoteDataSource,
    private val bookLocalDataSource: BookLocalDataSource,
) : BookRepository {
    override suspend fun getSearchBooks(
        query: String,
        page: Int,
        pageSize: Int,
    ): BookContainer {
        // 캐시 우선: (검색어, 페이지)로 캐싱된 결과가 있으면 API를 호출하지 않는다.
        val cache = bookLocalDataSource.getSearch(query, page)
        if (cache != null) return cache

        val container =
            bookRemoteDataSource
                .getSearchBooks(
                    query = query,
                    page = page,
                    pageSize = pageSize,
                ).toDomain()
        bookLocalDataSource.saveSearch(query, page, container)
        return container
    }

    override suspend fun getBook(itemId: Long): Book {
        bookLocalDataSource.getBook(itemId)?.let { return it }

        val book =
            bookRemoteDataSource
                .getBook(itemId)
                .toDomain()
                .books
                .first()
        bookLocalDataSource.saveBooks(listOf(book))
        return book
    }
}
