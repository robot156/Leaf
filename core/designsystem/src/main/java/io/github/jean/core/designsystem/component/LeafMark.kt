package io.github.jean.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafMark(
    iconSize: Dp,
    modifier: Modifier = Modifier,
    alpha: Float = 1f,
    ink: Color = LeafTheme.colors.primary,
    paper: Color = LeafTheme.colors.card,
) {
    Canvas(
        modifier =
            modifier
                .size(iconSize)
                .alpha(alpha),
    ) {
        val s = size.minDimension / 100f

        val body =
            Path().apply {
                moveTo(36 * s, 12 * s)
                lineTo(64 * s, 12 * s)
                lineTo(64 * s, 80 * s)
                lineTo(50 * s, 68 * s)
                lineTo(36 * s, 80 * s)
                close()
            }
        drawPath(path = body, color = ink)

        val strokeWidth = 2 * s

        fun vein(
            x1: Float,
            y1: Float,
            x2: Float,
            y2: Float,
        ) {
            drawLine(
                color = paper,
                start = Offset(x1 * s, y1 * s),
                end = Offset(x2 * s, y2 * s),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round,
            )
        }
        vein(50f, 20f, 50f, 62f)
        vein(50f, 32f, 40f, 42f)
        vein(50f, 32f, 60f, 42f)
        vein(50f, 46f, 42f, 54f)
        vein(50f, 46f, 58f, 54f)
    }
}

@ThemePreviews
@Composable
private fun LeafMarkPreview() {
    LeafTheme {
        LeafMark(56.dp)
    }
}
