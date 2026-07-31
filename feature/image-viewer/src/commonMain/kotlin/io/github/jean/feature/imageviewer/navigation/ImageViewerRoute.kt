package io.github.jean.feature.imageviewer.navigation

import io.github.jean.core.ui.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class ImageViewerRoute(
    val imageUrl: String,
) : Route
