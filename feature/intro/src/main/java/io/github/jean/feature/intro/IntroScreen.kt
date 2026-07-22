package io.github.jean.feature.intro

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafMark
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.intro.model.IntroIntent
import io.github.jean.feature.intro.model.IntroSideEffect
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun IntroRoute(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IntroViewModel = metroViewModel(),
) {
    viewModel.collectSideEffect { effect ->
        when (effect) {
            is IntroSideEffect.NavigateToHome -> {
                navigateToHome()
            }
        }
    }

    IntroScreen(
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun IntroScreen(
    onIntent: (IntroIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    LaunchedEffect(Unit) {
        delay(1500.milliseconds)
        onIntent(IntroIntent.LoadDone)
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(LeafTheme.colors.surface),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .graphicsLayer { alpha = contentAlpha.value },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LeafMark(iconSize = 72.dp)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = LeafTheme.typography.wordmark,
                color = LeafTheme.colors.textPrimary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.app_desc),
                style = LeafTheme.typography.tagline,
                color = LeafTheme.colors.textMuted,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun IntroScreenPreview() {
    LeafTheme {
        IntroScreen(
            onIntent = {},
        )
    }
}
