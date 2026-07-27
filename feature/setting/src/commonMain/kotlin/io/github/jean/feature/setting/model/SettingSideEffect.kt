package io.github.jean.feature.setting.model

sealed interface SettingSideEffect {
    data object NavigateToBack : SettingSideEffect

    data object NavigateToThemeSetting : SettingSideEffect

    data class NavigateToEmail(
        val appVersion: String,
        val osVersion: String,
        val deviceModel: String,
    ) : SettingSideEffect

    data object NavigateToLicenses : SettingSideEffect
}
