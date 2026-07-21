package io.github.jean.core.designsystem.component

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafOutlinedButton(
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
    val contentColor = if (enabled) colors.primary else colors.textMuted
    val borderColor = if (enabled) colors.primary else colors.border

    Row(
        modifier =
            modifier
                .pressScale(
                    interactionSource = interactionSource,
                    enabled = enabled,
                )
                .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
                .defaultMinSize(minHeight = 48.dp)
                .clip(12.dp)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    radius = 12.dp,
                )
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
                size = 14.dp,
                color = contentColor,
            )
        }

        Text(
            text = text,
            style = LeafTheme.typography.label,
            textAlign = TextAlign.Center,
            color = contentColor,
        )

        if (trailingIconRes != null) {
            LeafImage(
                model = trailingIconRes,
                size = 14.dp,
                color = contentColor,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun LeafOutlinedButtonPreview() {
    LeafTheme {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LeafOutlinedButton(
                text = "책 선택하기",
                leadingIconRes = LeafTheme.res.plus,
                onClick = {},
            )
            Spacer(Modifier.height(12.dp))
            LeafOutlinedButton(
                text = "책 선택하기",
                trailingIconRes = LeafTheme.res.plus,
                onClick = {},
            )
            Spacer(Modifier.height(12.dp))
            LeafOutlinedButton(
                text = "책 선택하기",
                leadingIconRes = LeafTheme.res.plus,
                onClick = {},
                enabled = false,
            )
        }
    }
}
