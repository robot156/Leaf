package io.github.jean.feature.settinglicense.detail.model

import io.github.jean.core.ui.mvi.Intent

sealed interface SettingLicenseDetailIntent : Intent {
    data object BackClick : SettingLicenseDetailIntent
}
