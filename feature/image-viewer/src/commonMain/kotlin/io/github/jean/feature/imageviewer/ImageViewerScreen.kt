package io.github.jean.feature.imageviewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import io.github.jean.core.designsystem.component.LeafIconButton
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.imageviewer.model.ImageViewerIntent
import io.github.jean.feature.imageviewer.model.ImageViewerSideEffect
import io.github.jean.feature.imageviewer.model.ImageViewerState
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ImageViewerRoute(
    imageUrl: String,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageViewerViewModel =
        assistedMetroViewModel<ImageViewerViewModel, ImageViewerViewModel.Factory> {
            create(imageUrl)
        },
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is ImageViewerSideEffect.NavigateToBack -> {
                navigateToBack()
            }
        }
    }

    ImageViewerScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun ImageViewerScreen(
    state: ImageViewerState,
    onIntent: (ImageViewerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val zoomState = rememberZoomState()
    val isZoomed by remember { derivedStateOf { zoomState.scale > 1f } }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Black),
    ) {
        LeafImage(
            model = state.imageUrl,
            scale = ContentScale.Fit,
            modifier =
                Modifier
                    .fillMaxSize()
                    .zoomable(zoomState),
        )

        AnimatedVisibility(
            visible = !isZoomed,
            enter = slideInVertically { fullHeight -> -fullHeight } + fadeIn(),
            exit = slideOutVertically { fullHeight -> -fullHeight } + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(56.dp)
                        .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LeafIconButton(
                    icon = LeafTheme.res.close,
                    onClick = { onIntent(ImageViewerIntent.CloseClick) },
                    tint = Color.White,
                )
            }
        }
    }
}
