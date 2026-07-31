package io.github.jean.feature.home.model

import io.github.jean.core.ui.mvi.Intent

sealed interface HomeIntent : Intent {
    data object RefreshClick : HomeIntent

    data object AddClick : HomeIntent

    data object MenuClick : HomeIntent

    data class BookNoteClick(
        val noteId: Long,
    ) : HomeIntent
}
