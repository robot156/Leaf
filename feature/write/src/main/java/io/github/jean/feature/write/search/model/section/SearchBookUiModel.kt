package io.github.jean.feature.write.search.model.section

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.Book
import io.github.jean.core.common.time.now
import kotlin.time.Instant

@Immutable
data class SearchBookUiModel(
    val itemId: Long,
    val title: String,
    val author: String,
    val publisher: String,
    val coverUrl: String,
    val isbn13: String,
    val description: String,
    val link: String,
    val createdAt: Instant,
) {
    companion object {
        val Mock =
            SearchBookUiModel(
                itemId = 1L,
                title = "페르난두 페소아 — 페소아와 페소아들",
                author = "김한민 엮고 옮김",
                publisher = "워크룸프레스",
                coverUrl = "",
                isbn13 = "9788994207490",
                description =
                    """
                    페소아가 여러 이명(異名)으로 남긴 시와 산문을 엮은 선집. 알베르투 카에이루, 리카르두 레이스, 알바루 드 캄푸스 — 한 사람 안에 살았던 여럿의 목소리를 김한민이 골라 옮겼다. "나는 내 안의 여럿과 함께 걷는다"라는 문장으로 요약되는 페소아 세계의 입문서이자 결정판.
                    """.trimIndent(),
                link = "https://www.aladin.co.kr",
                createdAt = now(),
            )
    }
}

fun Book.toUiModel(): SearchBookUiModel =
    SearchBookUiModel(
        itemId = itemId,
        title = title,
        author = author,
        publisher = publisher,
        coverUrl = cover,
        isbn13 = isbn13,
        description = description,
        link = link,
        createdAt = createdAt,
    )

fun SearchBookUiModel.toBook(): Book =
    Book(
        itemId = itemId,
        title = title,
        link = link,
        author = author,
        cover = coverUrl,
        description = description,
        isbn13 = isbn13,
        publisher = publisher,
        createdAt = createdAt,
    )
