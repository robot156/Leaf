package io.github.jean.core.datalocal.database.entity

import io.github.jean.core.common.model.NoteContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 노트를 구성하는 블록의 저장 표현. [BookNoteEntity.content]에 JSON으로 직렬화되어 저장된다.
 * 리스트 순서 = 화면 표시 순서. 다형성 판별자는 @SerialName.
 */
@Serializable
sealed interface NoteContentEntity {
    @Serializable
    @SerialName("quote")
    data class Quote(
        val page: Int?,
        val sentence: String,
    ) : NoteContentEntity

    @Serializable
    @SerialName("record")
    data class Record(
        val content: String,
    ) : NoteContentEntity
}

fun NoteContentEntity.toDomain(): NoteContent =
    when (this) {
        is NoteContentEntity.Quote -> {
            NoteContent.Quote(
                page = page,
                sentence = sentence,
            )
        }

        is NoteContentEntity.Record -> {
            NoteContent.Record(
                content = content,
            )
        }
    }

fun NoteContent.toEntity(): NoteContentEntity =
    when (this) {
        is NoteContent.Quote -> {
            NoteContentEntity.Quote(
                page = page,
                sentence = sentence,
            )
        }

        is NoteContent.Record -> {
            NoteContentEntity.Record(
                content = content,
            )
        }
    }
