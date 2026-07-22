package io.github.jean.feature.write.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import io.github.jean.core.ui.navigation.coverSlideTransition
import io.github.jean.feature.write.editor.EditorRoute
import io.github.jean.feature.write.search.SearchRoute

fun EntryProviderScope<NavKey>.editorEntry(
    navigateToBack: () -> Unit,
    navigateToSearch: () -> Unit,
    navigateToDetail: (noteId: Long) -> Unit,
) {
    entry<EditorRoute>(metadata = NavDisplay.coverSlideTransition()) { route ->
        EditorRoute(
            noteId = route.noteId,
            navigateToBack = navigateToBack,
            navigateToSearch = navigateToSearch,
            navigateToDetail = navigateToDetail,
        )
    }
}

fun EntryProviderScope<NavKey>.searchEntry(
    navigateToBack: () -> Unit,
    navigateToExternalWeb: (link: String) -> Unit,
    navigateToImageViewer: (imageUrl: String) -> Unit,
) {
    entry<SearchRoute>(metadata = NavDisplay.coverSlideTransition()) {
        SearchRoute(
            navigateToBack = navigateToBack,
            navigateToExternalWeb = navigateToExternalWeb,
            navigateToImageViewer = navigateToImageViewer,
        )
    }
}
