package io.github.jean.feature.main.model

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.common.model.Theme

@Immutable
data class MainState(
    val theme: Theme = Theme.System,
    val palette: LeafPalette = LeafPalette.InkNavy,
)
