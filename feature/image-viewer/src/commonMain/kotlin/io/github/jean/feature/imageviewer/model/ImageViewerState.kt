package io.github.jean.feature.imageviewer.model

import androidx.compose.runtime.Immutable

@Immutable
data class ImageViewerState(
    val imageUrl: String = "",
)
