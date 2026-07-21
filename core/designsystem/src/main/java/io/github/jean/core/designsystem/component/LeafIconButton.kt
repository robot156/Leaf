package io.github.jean.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = LeafTheme.colors.textPrimary,
    contentDescription: String? = null,
    iconSize: Dp = 24.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            modifier
                .pressScale(
                    interactionSource = interactionSource,
                    enabled = enabled,
                    pressedScale = 0.88f,
                ).size(44.dp)
                .clip(CircleShape)
                .throttleClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        LeafImage(
            model = icon,
            color = if (enabled) tint else LeafTheme.colors.textMuted,
            contentDescription = contentDescription,
            size = iconSize,
        )
    }
}

@ThemePreviews
@Composable
private fun LeafIconButtonPreview() {
    LeafTheme {
        Column {
            LeafIconButton(
                icon = LeafTheme.res.menu,
                contentDescription = "메뉴",
                onClick = {},
            )

            LeafIconButton(
                icon = LeafTheme.res.close,
                contentDescription = "닫기",
                onClick = {},
                enabled = false,
            )
        }
    }
}
