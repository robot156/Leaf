package io.github.jean.core.designsystem.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val EXIT_DURATION = 500
private const val ENTER_DURATION = 500
private const val ENTER_DELAY = 200

fun leafFadeThrough(): ContentTransform =
    fadeIn(tween(durationMillis = ENTER_DURATION, delayMillis = ENTER_DELAY)) togetherWith
        fadeOut(tween(durationMillis = EXIT_DURATION))

@Composable
fun <T> LeafAnimatedContent(
    targetState: T,
    modifier: Modifier = Modifier,
    label: String = "LeafAnimatedContent",
    content: @Composable AnimatedContentScope.(targetState: T) -> Unit,
) {
    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = { leafFadeThrough() },
        label = label,
        content = content,
    )
}
