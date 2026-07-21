package io.github.jean.feature.home.model

sealed interface HomeSideEffect {
    data object NavigateToSetting : HomeSideEffect

    data object NavigateToEditor : HomeSideEffect

    data class NavigateToDetail(
        val noteId: Long,
    ) : HomeSideEffect
}
