package io.github.jean.core.designsystem.modifier

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.dashedBorder(
    width: Dp,
    color: Color,
    radius: Dp = 0.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
): Modifier {
    if (width <= 0.dp) return this
    return drawWithCache {
        val strokeWidthPx = width.toPx()
        val stroke =
            Stroke(
                width = strokeWidthPx,
                pathEffect =
                    PathEffect.dashPathEffect(
                        intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
                        phase = 0f,
                    ),
            )
        val inset = strokeWidthPx / 2f

        onDrawBehind {
            drawRoundRect(
                color = color,
                topLeft = Offset(inset, inset),
                size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
                cornerRadius = CornerRadius((radius.toPx() - inset).coerceAtLeast(0f)),
                style = stroke,
            )
        }
    }
}
