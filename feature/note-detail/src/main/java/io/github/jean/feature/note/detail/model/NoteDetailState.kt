package io.github.jean.feature.note.detail.model

import androidx.compose.runtime.Immutable
import io.github.jean.feature.note.detail.model.section.NoteDetailBlockUiModel
import io.github.jean.feature.note.detail.model.section.NoteDetailBookUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class NoteDetailState(
    val noteId: Long = 0,
    val book: NoteDetailBookUiModel = NoteDetailBookUiModel(),
    val recordedDate: String = "",
    val blocks: ImmutableList<NoteDetailBlockUiModel> = persistentListOf(),
    val isShowBookDetailDialog: Boolean = false,
    val isShowEditDialog: Boolean = false,
    val isShared: Boolean = false,
)
