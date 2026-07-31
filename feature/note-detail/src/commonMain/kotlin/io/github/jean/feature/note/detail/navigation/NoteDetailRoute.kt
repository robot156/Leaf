package io.github.jean.feature.note.detail.navigation

import io.github.jean.core.ui.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class NoteDetailRoute(
    val noteId: Long,
) : Route
