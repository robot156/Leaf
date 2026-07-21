package io.github.jean.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.jean.feature.home.HomeRoute

fun EntryProviderScope<NavKey>.homeEntry(
    navigateToSetting: () -> Unit,
    navigateToDetail: (noteId: Long) -> Unit,
    navigateToEditor: () -> Unit,
) {
    entry<HomeRoute> {
        HomeRoute(
            navigateToSetting = navigateToSetting,
            navigateToDetail = navigateToDetail,
            navigateToEditor = navigateToEditor,
        )
    }
}
