package io.github.jean.feature.settingtheme.model

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.common.model.Theme

@Immutable
data class SettingThemeState(
    val currentTheme: Theme = Theme.System,
    val currentPalette: LeafPalette = LeafPalette.InkNavy,
)
