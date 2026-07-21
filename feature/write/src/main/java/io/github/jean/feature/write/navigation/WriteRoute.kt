package io.github.jean.feature.write.navigation

import io.github.jean.core.ui.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class EditorRoute(
    val noteId: Long? = null,
) : Route

@Serializable
data object SearchRoute : Route
