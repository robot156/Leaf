package io.github.jean.feature.imageviewer.model

sealed interface ImageViewerSideEffect {
    data object NavigateToBack : ImageViewerSideEffect
}
