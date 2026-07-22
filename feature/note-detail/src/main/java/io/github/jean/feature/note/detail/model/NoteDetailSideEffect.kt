package io.github.jean.feature.note.detail.model

import androidx.compose.ui.graphics.ImageBitmap

sealed interface NoteDetailSideEffect {
    data object NavigateToBack : NoteDetailSideEffect

    data class NavigateToEditor(
        val noteId: Long,
    ) : NoteDetailSideEffect

    data class NavigateToExternalWeb(
        val link: String,
    ) : NoteDetailSideEffect

    data class NavigateToShareRecord(
        val bitmap: ImageBitmap,
    ) : NoteDetailSideEffect
}
