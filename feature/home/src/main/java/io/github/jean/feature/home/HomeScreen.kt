package io.github.jean.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.animation.LeafAnimatedContent
import io.github.jean.core.designsystem.component.LeafTopNavigation
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.core.ui.LeafErrorScreen
import io.github.jean.feature.home.model.HomeIntent
import io.github.jean.feature.home.model.HomeSideEffect
import io.github.jean.feature.home.model.HomeState
import io.github.jean.feature.home.preview.HomeStatePreviewProvider
import io.github.jean.feature.home.ui.HomeBookListContent
import io.github.jean.feature.home.ui.HomeEmptyContent
import io.github.jean.feature.home.ui.HomeSkeletonContent
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private enum class HomeUiType {
    Loading,
    Error,
    Empty,
    List,
}

@Composable
fun HomeRoute(
    navigateToSetting: () -> Unit,
    navigateToDetail: (noteId: Long) -> Unit,
    navigateToEditor: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = metroViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.NavigateToSetting -> {
                navigateToSetting()
            }

            is HomeSideEffect.NavigateToDetail -> {
                navigateToDetail(sideEffect.noteId)
            }

            is HomeSideEffect.NavigateToEditor -> {
                navigateToEditor()
            }
        }
    }

    HomeScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val homeUiType =
        when {
            state.isLoading -> HomeUiType.Loading
            state.isError -> HomeUiType.Error
            state.bookNotes.isEmpty() -> HomeUiType.Empty
            else -> HomeUiType.List
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(LeafTheme.colors.surface)
                .systemBarsPadding(),
    ) {
        LeafTopNavigation(
            title = stringResource(R.string.app_name),
            titleStyle = LeafTheme.typography.wordmarkSmall,
            trailing =
                persistentListOf(
                    LeafTopNavItem.Icon(
                        iconRes = LeafTheme.res.menu,
                        onClick = { onIntent(HomeIntent.MenuClick) },
                    ),
                ),
        )

        LeafAnimatedContent(
            targetState = homeUiType,
            label = "home",
        ) { type ->
            when (type) {
                HomeUiType.Loading -> {
                    HomeSkeletonContent()
                }

                HomeUiType.Error -> {
                    LeafErrorScreen(
                        modifier = Modifier.padding(bottom = 56.dp),
                        onRetry = { onIntent(HomeIntent.RefreshClick) },
                    )
                }

                HomeUiType.Empty -> {
                    HomeEmptyContent(onIntent = onIntent)
                }

                HomeUiType.List -> {
                    HomeBookListContent(
                        bookNotes = state.bookNotes,
                        onIntent = onIntent,
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeStatePreviewProvider::class) state: HomeState,
) {
    LeafTheme {
        HomeScreen(
            state = state,
            onIntent = {},
        )
    }
}
