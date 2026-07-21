package io.github.jean.feature.setting.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun SettingsDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        thickness = 0.5.dp,
        color = LeafTheme.colors.border,
        modifier = modifier.padding(horizontal = 20.dp),
    )
}
