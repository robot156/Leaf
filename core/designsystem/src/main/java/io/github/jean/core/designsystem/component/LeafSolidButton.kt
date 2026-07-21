package io.github.jean.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafSolidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fillMaxWidth: Boolean = false,
    @DrawableRes leadingIconRes: Int? = null,
    @DrawableRes trailingIconRes: Int? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val colors = LeafTheme.colors
    val containerColor = if (enabled) colors.primary else colors.border
    val contentColor = if (enabled) colors.onPrimary else colors.textMuted

    Row(
        modifier =
            modifier
                .pressScale(
                    interactionSource = interactionSource,
                    enabled = enabled,
                )
                .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
                .defaultMinSize(minHeight = 48.dp)
                .clip(14.dp)
                .background(containerColor)
                .throttleClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(horizontal = 22.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIconRes != null) {
            LeafImage(
                model = leadingIconRes,
                color = contentColor,
                size = 14.dp,
            )
        }
        Text(
            text = text,
            style = LeafTheme.typography.label,
            color = contentColor,
        )

        if (trailingIconRes != null) {
            LeafImage(
                model = trailingIconRes,
                color = contentColor,
                size = 14.dp,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun LeafSolidButtonPreview() {
    LeafTheme {
        Column {
            LeafSolidButton(
                text = "첫 책 추가하기",
                onClick = {},
                leadingIconRes = LeafTheme.res.plus,
            )
            Spacer(Modifier.height(10.dp))

            LeafSolidButton(
                text = "첫 책 추가하기",
                onClick = {},
                trailingIconRes = LeafTheme.res.chevronRight,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun LeafSolidButtonDisabledPreview() {
    LeafTheme {
        LeafSolidButton(
            text = "저장",
            onClick = {},
            enabled = false,
        )
    }
}
