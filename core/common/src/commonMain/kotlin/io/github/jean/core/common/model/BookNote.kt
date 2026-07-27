package io.github.jean.core.common.model

import kotlin.time.Instant

data class BookNote(
    val id: Long = 0L,
    val bookId: Long,
    val content: List<NoteContent> = emptyList(),
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    /** 저장 가능 여부 — 비어있지 않은 블록이 하나라도 있어야 한다 */
    val isSavable: Boolean
        get() =
            content.any {
                when (it) {
                    is NoteContent.Quote -> it.sentence.isNotBlank()
                    is NoteContent.Record -> it.content.isNotBlank()
                }
            }
}
