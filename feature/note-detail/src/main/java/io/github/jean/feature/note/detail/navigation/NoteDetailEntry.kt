package io.github.jean.feature.note.detail.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import io.github.jean.core.ui.navigation.coverSlideTransition
import io.github.jean.feature.note.detail.NoteDetailRoute

fun EntryProviderScope<NavKey>.noteDetailEntry(
    navigateToBack: () -> Unit,
    navigateToEditor: (noteId: Long) -> Unit,
    navigateToExternalWeb: (link: String) -> Unit,
) {
    entry<NoteDetailRoute>(metadata = NavDisplay.coverSlideTransition()) { route ->
        NoteDetailRoute(
            noteId = route.noteId,
            navigateToBack = navigateToBack,
            navigateToEditor = navigateToEditor,
            navigateToExternalWeb = navigateToExternalWeb,
        )
    }
}
