package io.github.jean.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes trailingIconRes: Int? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = LeafTheme.colors
    val textColor = if (enabled) colors.textSecondary else colors.textMuted

    Row(
        modifier =
            modifier
                .pressScale(
                    interactionSource = interactionSource,
                    enabled = enabled,
                    pressedScale = 0.94f,
                ).clip(14.dp)
                .border(
                    width = 0.5.dp,
                    color = colors.border,
                    radius = 14.dp,
                ).throttleClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                ).padding(horizontal = 11.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = LeafTheme.typography.meta,
            color = textColor,
        )
        if (trailingIconRes != null) {
            LeafImage(
                model = trailingIconRes,
                color = colors.textMuted,
                size = 10.dp,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun LeafChipPreview() {
    LeafTheme {
        LeafChip(
            modifier = Modifier.padding(12.dp),
            text = "2026년 7월 12일",
            onClick = {},
            trailingIconRes = LeafTheme.res.chevronDown,
        )
    }
}
