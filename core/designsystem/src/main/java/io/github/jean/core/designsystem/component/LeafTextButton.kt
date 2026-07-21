package io.github.jean.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    typography: TextStyle = LeafTheme.typography.label,
    color: Color = LeafTheme.colors.primary,
    @DrawableRes leadingIconRes: Int? = null,
    @DrawableRes trailingIconRes: Int? = null,
    horizontalTouchSize: Dp = 10.dp,
    verticalTouchSize: Dp = 8.dp,
    iconSize: Dp? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val contentColor = if (enabled) color else LeafTheme.colors.textMuted

    Row(
        modifier =
            modifier
                .pressScale(
                    interactionSource = interactionSource,
                    enabled = enabled,
                    pressedScale = 0.94f,
                )
                .clip(8.dp)
                .throttleClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(
                    horizontal = horizontalTouchSize,
                    vertical = verticalTouchSize,
                ),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIconRes != null) {
            LeafImage(
                model = leadingIconRes,
                color = contentColor,
                size = iconSize ?: 16.dp,
            )
        }
        Text(
            text = text,
            style = typography,
            color = contentColor,
        )
        if (trailingIconRes != null) {
            LeafImage(
                model = trailingIconRes,
                color = contentColor,
                size = iconSize ?: 16.dp,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun LeafTextButtonPreview() {
    LeafTheme {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LeafTextButton(
                text = "저장",
                onClick = {},
            )
            LeafTextButton(
                text = "저장",
                onClick = {},
                enabled = false,
            )
            LeafTextButton(
                text = "추가",
                onClick = {},
                leadingIconRes = LeafTheme.res.plus,
            )
            LeafTextButton(
                text = "날짜",
                onClick = {},
                trailingIconRes = LeafTheme.res.chevronDown,
            )
            LeafTextButton(
                text = "상세보기",
                typography = LeafTheme.typography.meta,
                trailingIconRes = LeafTheme.res.chevronRight,
                iconSize = 8.dp,
                horizontalTouchSize = 0.dp,
                onClick = {},
            )
        }
    }
}
