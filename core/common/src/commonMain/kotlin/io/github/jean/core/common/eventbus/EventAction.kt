package io.github.jean.core.common.eventbus

import io.github.jean.core.common.model.Book

sealed interface EventAction

sealed interface BookSelectedAction : EventAction {
    data class BookSelected(
        val book: Book,
    ) : BookSelectedAction
}
