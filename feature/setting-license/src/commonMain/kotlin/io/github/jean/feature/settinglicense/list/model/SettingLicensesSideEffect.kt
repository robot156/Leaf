package io.github.jean.feature.settinglicense.list.model

sealed interface SettingLicensesSideEffect {
    data object NavigateToBack : SettingLicensesSideEffect

    data class NavigateToLicenseDetail(
        val uniqueId: String,
    ) : SettingLicensesSideEffect
}
