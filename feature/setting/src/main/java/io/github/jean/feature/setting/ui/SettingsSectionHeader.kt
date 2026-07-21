package io.github.jean.feature.setting.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun SettingsSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = LeafTheme.typography.label,
        color = LeafTheme.colors.textMuted,
        modifier =
            modifier
                .padding(horizontal = 20.dp)
                .padding(top = 18.dp, bottom = 6.dp),
    )
}
