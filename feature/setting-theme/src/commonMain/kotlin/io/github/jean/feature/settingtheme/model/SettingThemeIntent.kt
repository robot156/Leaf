package io.github.jean.feature.settingtheme.model

import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.common.model.Theme
import io.github.jean.core.ui.mvi.Intent

sealed interface SettingThemeIntent : Intent {
    data object BackClick : SettingThemeIntent

    data class ThemeSelect(
        val theme: Theme,
    ) : SettingThemeIntent

    data class PaletteSelect(
        val palette: LeafPalette,
    ) : SettingThemeIntent
}
