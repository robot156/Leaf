package io.github.jean.core.datalocal.datasource.book

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.model.Book
import io.github.jean.core.common.model.BookContainer
import io.github.jean.core.common.time.now
import io.github.jean.core.datalocal.database.dao.BookDao
import io.github.jean.core.datalocal.database.dao.BookSearchCacheDao
import io.github.jean.core.datalocal.database.entity.BookEntity
import io.github.jean.core.datalocal.database.entity.BookSearchCacheEntity
import io.github.jean.core.datalocal.database.entity.toDomain
import io.github.jean.core.datalocal.database.entity.toEntity
import kotlin.time.Duration

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class BookLocalDataSourceImpl(
    private val bookDao: BookDao,
    private val bookSearchCacheDao: BookSearchCacheDao,
) : BookLocalDataSource {
    override suspend fun saveBooks(books: List<Book>) {
        bookDao.upsertAll(books.map(Book::toEntity))
    }

    override suspend fun getBooks(): List<Book> = bookDao.getAll().map(BookEntity::toDomain)

    override suspend fun getBook(itemId: Long): Book? = bookDao.findByItemId(itemId)?.toDomain()

    override suspend fun getSearch(
        query: String,
        page: Int,
        ttl: Duration,
    ): BookContainer? {
        val cache = bookSearchCacheDao.findPage(query, page) ?: return null
        // TTL 초과 시 만료된 캐시로 간주. 다음 saveSearch에서 REPLACE로 덮어쓴다.
        if (now() - cache.createdAt > ttl) return null
        val booksByItemId = bookDao.findByItemIds(cache.itemIds).associateBy { it.itemId }
        // 캐시에 기록된 itemId 순서(= API 응답 순서)를 유지해 복원한다.
        val books = cache.itemIds.mapNotNull { booksByItemId[it]?.toDomain() }
        return BookContainer(
            totalResults = cache.totalResults,
            startIndex = cache.startIndex,
            itemsPerPage = cache.itemsPerPage,
            query = query,
            books = books,
        )
    }

    override suspend fun saveSearch(
        query: String,
        page: Int,
        container: BookContainer,
    ) {
        bookDao.upsertAll(container.books.map(Book::toEntity))
        bookSearchCacheDao.upsertPage(
            BookSearchCacheEntity(
                query = query,
                page = page,
                itemIds = container.books.map(Book::itemId),
                totalResults = container.totalResults,
                startIndex = container.startIndex,
                itemsPerPage = container.itemsPerPage,
                createdAt = now(),
            ),
        )
    }

    override suspend fun clearSearchCache() {
        bookSearchCacheDao.deleteAll()
    }
}
