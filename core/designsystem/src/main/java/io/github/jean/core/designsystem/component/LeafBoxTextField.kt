package io.github.jean.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafBoxTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    enabled: Boolean = true,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val colors = LeafTheme.colors

    val borderColor by animateColorAsState(
        targetValue = if (focused) colors.primary else colors.border,
        label = "leafTextFieldBorder",
    )

    BasicTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        textStyle = LeafTheme.typography.body.copy(color = colors.textPrimary),
        cursorBrush = SolidColor(colors.primary),
        lineLimits = lineLimits,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        interactionSource = interactionSource,
        decorator = { innerTextField ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(12.dp)
                        .background(colors.card)
                        .border(
                            width = 0.5.dp,
                            color = borderColor,
                            radius = 12.dp,
                        )
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(leadingIcon),
                        contentDescription = null,
                        tint = colors.textMuted,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (placeholder != null && state.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = LeafTheme.typography.body,
                            color = colors.textMuted,
                        )
                    }
                    innerTextField()
                }
            }
        },
    )
}

@ThemePreviews
@Composable
private fun LeafBoxTextFieldPreview() {
    LeafTheme {
        LeafBoxTextField(
            modifier = Modifier.padding(12.dp),
            state = rememberTextFieldState(),
            placeholder = "책 제목이나 저자를 검색해보세요",
            leadingIcon = LeafTheme.res.search,
        )
    }
}

@ThemePreviews
@Composable
private fun LeafBoxTextFieldFilledPreview() {
    LeafTheme {
        LeafBoxTextField(
            modifier = Modifier.padding(12.dp),
            state = rememberTextFieldState(initialText = "나는 유령작가 입니다"),
            leadingIcon = LeafTheme.res.search,
        )
    }
}
