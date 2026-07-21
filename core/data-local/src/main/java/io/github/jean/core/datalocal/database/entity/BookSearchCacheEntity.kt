package io.github.jean.core.datalocal.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlin.time.Instant

/**
 * 검색 결과를 (검색어, 페이지) 단위로 캐싱한다.
 * 실제 도서 데이터는 book 테이블에 저장하고, 여기에는 해당 페이지의 itemId 순서와 페이징 메타만 둔다.
 */
@Entity(
    tableName = "book_search_cache",
    primaryKeys = ["search_query", "page"],
)
data class BookSearchCacheEntity(
    @ColumnInfo(name = "search_query")
    val query: String,
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "item_ids")
    val itemIds: List<Long>,
    @ColumnInfo(name = "total_results")
    val totalResults: Int,
    @ColumnInfo(name = "start_index")
    val startIndex: Int,
    @ColumnInfo(name = "items_per_page")
    val itemsPerPage: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
)
