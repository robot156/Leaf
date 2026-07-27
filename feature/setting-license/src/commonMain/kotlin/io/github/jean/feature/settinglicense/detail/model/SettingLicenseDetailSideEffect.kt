package io.github.jean.feature.settinglicense.detail.model

sealed interface SettingLicenseDetailSideEffect {
    data object NavigateToBack : SettingLicenseDetailSideEffect
}
