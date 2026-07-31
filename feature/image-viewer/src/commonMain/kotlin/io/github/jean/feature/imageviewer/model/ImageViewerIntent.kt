package io.github.jean.feature.imageviewer.model

import io.github.jean.core.ui.mvi.Intent

sealed interface ImageViewerIntent : Intent {
    data object CloseClick : ImageViewerIntent
}
