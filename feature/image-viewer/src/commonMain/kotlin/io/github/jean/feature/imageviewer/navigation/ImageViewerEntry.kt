package io.github.jean.feature.imageviewer.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import io.github.jean.core.ui.navigation.coverSlideUpTransition
import io.github.jean.feature.imageviewer.ImageViewerRoute

fun EntryProviderScope<NavKey>.imageViewerEntry(
    navigateToBack: () -> Unit,
) {
    entry<ImageViewerRoute>(metadata = NavDisplay.coverSlideUpTransition()) { route ->
        ImageViewerRoute(
            imageUrl = route.imageUrl,
            navigateToBack = navigateToBack,
        )
    }
}
