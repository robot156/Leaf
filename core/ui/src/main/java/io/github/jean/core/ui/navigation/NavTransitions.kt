package io.github.jean.core.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay

private const val DEFAULT_TRANSITION_DURATION = 400

fun NavDisplay.coverSlideTransition(durationMillis: Int = DEFAULT_TRANSITION_DURATION): Map<String, Any> =
    transitionSpec {
        (
            slideInHorizontally(tween(durationMillis), initialOffsetX = { it }) togetherWith
                slideOutHorizontally(tween(durationMillis), targetOffsetX = { 0 })
        ).apply { targetContentZIndex = 1f }
    } +
        popTransitionSpec {
            slideInHorizontally(tween(durationMillis), initialOffsetX = { 0 }) togetherWith
                slideOutHorizontally(tween(durationMillis), targetOffsetX = { it })
        } +
        predictivePopTransitionSpec { _ ->
            slideInHorizontally(tween(durationMillis), initialOffsetX = { 0 }) togetherWith
                slideOutHorizontally(tween(durationMillis), targetOffsetX = { it })
        }
