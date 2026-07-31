package io.github.jean.feature.settinglicense.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import io.github.jean.core.ui.navigation.coverSlideTransition
import io.github.jean.feature.settinglicense.detail.SettingLicenseDetailRoute
import io.github.jean.feature.settinglicense.list.SettingLicensesRoute

fun EntryProviderScope<NavKey>.settingLicensesEntry(
    navigateToBack: () -> Unit,
    navigateToLicenseDetail: (uniqueId: String) -> Unit,
) {
    entry<SettingLicensesRoute>(metadata = NavDisplay.coverSlideTransition()) {
        SettingLicensesRoute(
            navigateToBack = navigateToBack,
            navigateToLicenseDetail = navigateToLicenseDetail,
        )
    }
}

fun EntryProviderScope<NavKey>.settingLicenseDetailEntry(navigateToBack: () -> Unit) {
    entry<SettingLicenseDetailRoute>(metadata = NavDisplay.coverSlideTransition()) { route ->
        SettingLicenseDetailRoute(
            uniqueId = route.uniqueId,
            navigateToBack = navigateToBack,
        )
    }
}
