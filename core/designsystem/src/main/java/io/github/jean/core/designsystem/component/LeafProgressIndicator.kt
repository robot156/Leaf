package io.github.jean.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
    strokeWidth: Dp = 4.dp,
    color: Color = LeafTheme.colors.textSecondary,
) {
    Box(modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .size(size)
                    .clip(CircleShape)
                    .padding(strokeWidth)
                    .border(BorderStroke(strokeWidth, LeafTheme.colors.border), CircleShape),
        )

        CircularProgressIndicator(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .size(size)
                    .padding(strokeWidth),
            strokeWidth = strokeWidth,
            color = color,
        )
    }
}

@ThemePreviews
@Composable
private fun LeafProgressIndicatorPreview() {
    LeafTheme {
        LeafProgressIndicator()
    }
}
