package io.github.jean.core.designsystem.modifier

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.theme.LeafTheme

fun Modifier.skeletonPulse(radius: Dp = 6.dp): Modifier =
    composed {
        val transition = rememberInfiniteTransition(label = "skeletonPulse")
        val alpha by transition.animateFloat(
            initialValue = 0.4f,
            targetValue = 0.75f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(700),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "skeletonAlpha",
        )
        graphicsLayer { this.alpha = alpha }
            .clip(radius)
            .background(LeafTheme.colors.skeleton)
    }
