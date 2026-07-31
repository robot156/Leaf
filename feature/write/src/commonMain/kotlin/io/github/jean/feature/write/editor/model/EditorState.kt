package io.github.jean.feature.write.editor.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import io.github.jean.core.common.model.NoteContent
import io.github.jean.core.common.time.LeafDateFormat
import io.github.jean.core.common.time.now
import io.github.jean.feature.write.editor.model.section.EditorBlock
import io.github.jean.feature.write.editor.model.section.EditorBookUiModel
import io.github.jean.feature.write.editor.model.section.toNoteContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

@Stable
data class EditorState(
    val noteId: Long? = null,
    val noteDate: LocalDate = LocalDate.now(),
    val noteDateText: String = LeafDateFormat.displayVerbose(now()),
    val book: EditorBookUiModel? = null,
    val quotes: ImmutableList<EditorBlock.Quote> = persistentListOf(),
    val recordState: TextFieldState = TextFieldState(),
    val focusedBlockId: Long? = null,
    val isShowDatePicker: Boolean = false,
    val isShowExitDialog: Boolean = false,
    val initialSnapshot: EditorSnapshot? = null,
) {
    val wordCount: Int
        get() = recordState.text.length + quotes.sumOf { it.sentence.length }

    val isSaveEnable: Boolean
        get() = book != null && (recordState.text.isNotBlank() || quotes.any { it.sentence.isNotBlank() })

    val noteContent: List<NoteContent>
        get() {
            val recordBlock = EditorBlock.Record(id = RECORD_BLOCK_ID, content = recordState.text.toString())
            return (quotes + recordBlock).toNoteContent()
        }

    val snapshot: EditorSnapshot
        get() = EditorSnapshot(bookId = book?.bookId, noteDate = noteDate, content = noteContent)

    val hasUnsavedChanges: Boolean
        get() = initialSnapshot != null && snapshot != initialSnapshot

    companion object {
        const val RECORD_BLOCK_ID = 0L
    }
}
