package io.github.jean.feature.settingtheme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.core.designsystem.theme.colors

@Composable
fun SettingThemePaletteRow(
    currentPalette: LeafPalette,
    onPaletteSelect: (LeafPalette) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(LeafPalette.entries) { palette ->
            PaletteSwatch(
                palette = palette,
                isDark = LeafTheme.isDarkTheme,
                selected = palette == currentPalette,
                onClick = { onPaletteSelect(palette) },
            )
        }
    }
}

@Composable
private fun PaletteSwatch(
    palette: LeafPalette,
    isDark: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val swatchColors = palette.colors(isDarkTheme = isDark)
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(52.dp)
                    .pressScale(interactionSource = interactionSource, pressedScale = 0.92f)
                    .clip(16.dp)
                    .background(swatchColors.surface)
                    .border(
                        width = if (selected) 2.dp else 0.5.dp,
                        color = if (selected) swatchColors.primary else LeafTheme.colors.border,
                        radius = 16.dp,
                    ).clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.RadioButton,
                        onClick = onClick,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(28.dp)
                        .clip(14.dp)
                        .background(swatchColors.primary),
                contentAlignment = Alignment.Center,
            ) {
                if (selected) {
                    LeafImage(
                        model = LeafTheme.res.check,
                        color = swatchColors.onPrimary,
                        size = 14.dp,
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = palette.getDisplayName(),
            style = LeafTheme.typography.meta,
            color =
                if (selected) {
                    LeafTheme.colors.textPrimary
                } else {
                    LeafTheme.colors.textMuted
                },
        )
    }
}

@Composable
private fun LeafPalette.getDisplayName(): String =
    when (this) {
        LeafPalette.InkNavy -> stringResource(R.string.setting_theme_palette_ink_navy)
        LeafPalette.Forest -> stringResource(R.string.setting_theme_palette_forest)
        LeafPalette.Burgundy -> stringResource(R.string.setting_theme_palette_burgundy)
        LeafPalette.Muk -> stringResource(R.string.setting_theme_palette_muk)
        LeafPalette.Olive -> stringResource(R.string.setting_theme_palette_olive)
        LeafPalette.Terracotta -> stringResource(R.string.setting_theme_palette_terracotta)
    }

@ThemePreviews
@Composable
private fun Preview() {
    LeafTheme {
        SettingThemePaletteRow(
            currentPalette = LeafPalette.InkNavy,
            onPaletteSelect = {},
        )
    }
}
