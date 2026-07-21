package io.github.jean.feature.settinglicense.navigation

import io.github.jean.core.ui.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object SettingLicensesRoute : Route

@Serializable
data class SettingLicenseDetailRoute(
    val uniqueId: String,
) : Route
