package io.github.jean.feature.note.detail.model

import io.github.jean.core.ui.mvi.Intent

sealed interface NoteDetailIntent : Intent {
    data object BackClick : NoteDetailIntent

    data object EditClick : NoteDetailIntent

    data object DeleteClick : NoteDetailIntent

    data object DeleteConfirmClick : NoteDetailIntent

    data object CancelClick : NoteDetailIntent

    data object DeleteDialogDismiss : NoteDetailIntent

    data object BookHeaderClick : NoteDetailIntent

    data object BookDetailDialogHide : NoteDetailIntent

    data class ExternalWebSiteClick(
        val webLink: String,
    ) : NoteDetailIntent
}
