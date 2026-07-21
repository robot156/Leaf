package io.github.jean.feature.settingtheme.model

sealed interface SettingThemeSideEffect {
    data object NavigateToBack : SettingThemeSideEffect
}
