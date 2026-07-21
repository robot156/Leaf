package io.github.jean.core.designsystem.modifier

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.border(
    width: Dp,
    color: Color,
    radius: Dp = 0.dp,
): Modifier {
    require(width > 0.dp) { "border width는 0dp보다 커야 합니다: $width" }
    return border(width = width, color = color, shape = RoundedCornerShape(radius))
}
