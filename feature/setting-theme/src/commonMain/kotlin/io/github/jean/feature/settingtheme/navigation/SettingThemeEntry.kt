package io.github.jean.feature.settingtheme.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import io.github.jean.core.ui.navigation.coverSlideTransition
import io.github.jean.feature.settingtheme.SettingThemeRoute

fun EntryProviderScope<NavKey>.settingThemeEntry(navigateToBack: () -> Unit) {
    entry<SettingThemeRoute>(metadata = NavDisplay.coverSlideTransition()) {
        SettingThemeRoute(
            navigateToBack = navigateToBack,
        )
    }
}
