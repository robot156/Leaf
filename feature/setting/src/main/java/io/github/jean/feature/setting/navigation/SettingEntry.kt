package io.github.jean.feature.setting.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import io.github.jean.core.ui.navigation.coverSlideTransition
import io.github.jean.feature.setting.SettingRoute

fun EntryProviderScope<NavKey>.settingEntry(
    navigateToBack: () -> Unit,
    navigateToThemeSetting: () -> Unit,
    navigateToEmail: (
        appVersion: String,
        osVersion: String,
        deviceModel: String,
    ) -> Unit,
    navigateToLicenses: () -> Unit,
) {
    entry<SettingRoute>(metadata = NavDisplay.coverSlideTransition()) {
        SettingRoute(
            navigateToBack = navigateToBack,
            navigateToThemeSetting = navigateToThemeSetting,
            navigateToEmail = navigateToEmail,
            navigateToLicenses = navigateToLicenses,
        )
    }
}
