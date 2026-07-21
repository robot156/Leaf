package io.github.jean.feature.setting.model

import io.github.jean.core.ui.mvi.Intent

sealed interface SettingIntent : Intent {
    data object BackClick : SettingIntent

    data object ThemeClick : SettingIntent

    data object ClearCacheClick : SettingIntent

    data object ClearCacheDialogDismiss : SettingIntent

    data object ClearCacheConfirm : SettingIntent

    data object ContactClick : SettingIntent

    data object LicensesClick : SettingIntent
}
