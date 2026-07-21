package io.github.jean.feature.note.detail.model.section

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.NoteContent

@Immutable
sealed interface NoteDetailBlockUiModel {
    data class Record(
        val content: String,
    ) : NoteDetailBlockUiModel

    data class Quote(
        val page: Int?,
        val sentence: String,
    ) : NoteDetailBlockUiModel
}

fun NoteContent.toUiModel(): NoteDetailBlockUiModel =
    when (this) {
        is NoteContent.Record -> NoteDetailBlockUiModel.Record(content = content)
        is NoteContent.Quote -> NoteDetailBlockUiModel.Quote(page = page, sentence = sentence)
    }
