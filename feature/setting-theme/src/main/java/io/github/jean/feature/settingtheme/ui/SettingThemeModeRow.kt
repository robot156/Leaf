package io.github.jean.feature.settingtheme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.jean.core.common.model.Theme
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun SettingThemeModeRow(
    theme: Theme,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LeafTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val contentColor = if (selected) colors.primary else colors.textMuted

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .pressScale(interactionSource = interactionSource, pressedScale = 0.98f)
                .clip(10.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.RadioButton,
                    onClick = onClick,
                ).padding(horizontal = 4.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(18.dp)
                    .clip(9.dp)
                    .border(width = 1.5.dp, color = contentColor, radius = 9.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Box(
                    modifier =
                        Modifier
                            .size(9.dp)
                            .clip(4.5.dp)
                            .background(colors.primary),
                )
            }
        }

        LeafImage(
            model = theme.iconRes(),
            contentDescription = null,
            color = contentColor,
            size = 20.dp,
        )

        Text(
            text = theme.getDisplayName(),
            style = LeafTheme.typography.body,
            color = if (selected) colors.textPrimary else colors.textMuted,
        )
    }
}

@Composable
private fun Theme.iconRes(): Int =
    when (this) {
        Theme.System -> LeafTheme.res.themeAuto
        Theme.Light -> LeafTheme.res.sun
        Theme.Dark -> LeafTheme.res.moon
    }

@Composable
private fun Theme.getDisplayName(): String =
    when (this) {
        Theme.System -> stringResource(R.string.setting_theme_system_follow)
        Theme.Light -> stringResource(R.string.setting_theme_light)
        Theme.Dark -> stringResource(R.string.setting_theme_dark)
    }
