package io.github.jean.feature.settingtheme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.component.LeafMark
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun SettingThemeHero(modifier: Modifier = Modifier) {
    val colors = LeafTheme.colors

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(18.dp)
                .background(colors.card)
                .border(width = 0.5.dp, color = colors.border, radius = 18.dp)
                .padding(vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LeafMark(iconSize = 84.dp)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.setting_theme_sample_title),
            style = LeafTheme.typography.wordmarkSmall,
            color = colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.setting_theme_sample_description),
            style = LeafTheme.typography.meta,
            color = colors.textMuted,
        )
    }
}
