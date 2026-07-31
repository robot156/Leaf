package io.github.jean.core.designsystem.modifier

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.clip(size: Dp): Modifier {
    if (size <= 0.dp) return this

    val shape = if (size == Int.MAX_VALUE.dp) CircleShape else RoundedCornerShape(size)
    return graphicsLayer(shape = shape, clip = true)
}
