package io.github.jean.feature.write.search.model

import io.github.jean.core.ui.mvi.Intent
import io.github.jean.feature.write.search.model.section.SearchBookUiModel

sealed interface SearchIntent : Intent {
    data object BackClick : SearchIntent

    data object RefreshClick : SearchIntent

    data object SearchClick : SearchIntent

    data object NextPageLoad : SearchIntent

    data class BookClick(
        val book: SearchBookUiModel,
    ) : SearchIntent

    data class BookDetailClick(
        val book: SearchBookUiModel,
    ) : SearchIntent

    data object BookDetailDialogHide : SearchIntent

    data class ExternalWebSiteClick(
        val webLink: String,
    ) : SearchIntent
}
