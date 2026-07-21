package io.github.jean.feature.note.detail.model

sealed interface NoteDetailSideEffect {
    data object NavigateToBack : NoteDetailSideEffect

    data class NavigateToEditor(
        val noteId: Long,
    ) : NoteDetailSideEffect

    data class NavigateToExternalWeb(
        val link: String,
    ) : NoteDetailSideEffect
}
