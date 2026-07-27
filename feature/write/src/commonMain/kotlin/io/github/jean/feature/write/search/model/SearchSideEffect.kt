package io.github.jean.feature.write.search.model

sealed interface SearchSideEffect {
    data object NavigateToBack : SearchSideEffect

    data class NavigateToExternalWeb(
        val link: String,
    ) : SearchSideEffect

    data class NavigateToImageViewer(
        val imageUrl: String,
    ) : SearchSideEffect
}
