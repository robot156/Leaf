package io.github.jean.core.dataremote.model

import io.github.jean.core.common.model.Book
import io.github.jean.core.common.model.BookContainer
import io.github.jean.core.common.time.now
import io.github.jean.core.dataremote.util.unescapeHtml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * 도서 검색 API 응답. 페이징 메타 정보와 검색 결과 목록을 함께 담는다.
 *
 * @property totalResults 전체 검색 결과 수
 * @property startIndex 현재 페이지의 시작 인덱스(1-based)
 * @property itemsPerPage 페이지당 결과 수
 * @property query 검색어
 * @property books 현재 페이지의 도서 목록
 */
@Serializable
data class BookContainerResponse(
    @SerialName("totalResults")
    val totalResults: Int = 0,
    @SerialName("startIndex")
    val startIndex: Int = 0,
    @SerialName("itemsPerPage")
    val itemsPerPage: Int = 0,
    @SerialName("query")
    val query: String = "",
    @SerialName("item")
    val books: List<BookResponse> = emptyList(),
)

fun BookContainerResponse.toDomain(): BookContainer =
    BookContainer(
        totalResults = totalResults,
        startIndex = startIndex,
        itemsPerPage = itemsPerPage,
        query = query,
        books = books.map(BookResponse::toDomain),
    )

/**
 * 검색 결과의 개별 도서 항목. 저장 대상 필드만 정의한다.
 */
@Serializable
data class BookResponse(
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("link")
    val link: String,
    @SerialName("author")
    val author: String,
    @SerialName("cover")
    val cover: String,
    @SerialName("description")
    val description: String,
    @SerialName("isbn13")
    val isbn13: String,
    @SerialName("publisher")
    val publisher: String,
)

fun BookResponse.toDomain(createdAt: Instant = now()): Book =
    Book(
        itemId = itemId,
        title = title.substringBefore(" - ").unescapeHtml(),
        link = link,
        author = author.substringBefore("(").trim(),
        cover = cover.replace("coversum", "cover500"),
        description = description.unescapeHtml(),
        isbn13 = isbn13,
        publisher = publisher,
        createdAt = createdAt,
    )
