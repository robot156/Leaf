package io.github.jean.feature.intro.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.jean.feature.intro.IntroRoute

fun EntryProviderScope<NavKey>.introEntry(navigateToHome: () -> Unit) {
    entry<IntroRoute> {
        IntroRoute(
            navigateToHome = navigateToHome,
        )
    }
}
