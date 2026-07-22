package io.github.jean.feature.write.search

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafTopNavigation
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.core.ui.LeafErrorScreen
import io.github.jean.core.ui.animation.FadeAnimatedVisibility
import io.github.jean.core.ui.paging.PaginationEffect
import io.github.jean.core.ui.paging.PagingErrorFooter
import io.github.jean.core.ui.paging.PagingLoadingFooter
import io.github.jean.feature.write.search.model.PagingState
import io.github.jean.feature.write.search.model.SearchIntent
import io.github.jean.feature.write.search.model.SearchSideEffect
import io.github.jean.feature.write.search.model.SearchState
import io.github.jean.feature.write.search.model.section.SearchBookUiModel
import io.github.jean.feature.write.search.ui.SearchBookDetailBottomSheet
import io.github.jean.feature.write.search.ui.SearchEmptyContent
import io.github.jean.feature.write.search.ui.SearchListItem
import io.github.jean.feature.write.search.ui.SearchSkeletonContent
import io.github.jean.feature.write.search.ui.SearchTextField
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchRoute(
    navigateToBack: () -> Unit,
    navigateToExternalWeb: (link: String) -> Unit,
    navigateToImageViewer: (imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = metroViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SearchSideEffect.NavigateToBack -> {
                navigateToBack()
            }

            is SearchSideEffect.NavigateToExternalWeb -> {
                navigateToExternalWeb(sideEffect.link)
            }

            is SearchSideEffect.NavigateToImageViewer -> {
                navigateToImageViewer(sideEffect.imageUrl)
            }
        }
    }

    SearchScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun SearchScreen(
    state: SearchState,
    onIntent: (SearchIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val paging = state.pagingState
    val focusManager = LocalFocusManager.current
    val onIntentWithDismiss: (SearchIntent) -> Unit =
        remember(onIntent, focusManager) {
            {
                intent ->
                focusManager.clearFocus()
                onIntent(intent)
            }
        }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(LeafTheme.colors.surface)
                .systemBarsPadding()
                .imePadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
    ) {
        LeafTopNavigation(
            leading =
                LeafTopNavItem.Icon(
                    iconRes = LeafTheme.res.chevronLeft,
                    onClick = { onIntent(SearchIntent.BackClick) },
                ),
            title = stringResource(R.string.search_title),
        )

        SearchTextField(
            state = state.searchTextState,
            onSearch = { onIntentWithDismiss(SearchIntent.SearchClick) },
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            FadeAnimatedVisibility(paging.isLoading) {
                SearchSkeletonContent()
            }

            FadeAnimatedVisibility(paging.isError) {
                LeafErrorScreen(onRetry = { onIntent(SearchIntent.RefreshClick) })
            }

            FadeAnimatedVisibility(paging.isSuccess && state.books.isNotEmpty()) {
                PaginationEffect(
                    listState = listState,
                    threshold = 3,
                    enabled = !paging.isLast && !paging.isErrorFooter,
                    onNext = { onIntent(SearchIntent.NextPageLoad) },
                )

                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                    state = listState,
                    contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp),
                ) {
                    itemsIndexed(
                        items = state.books,
                        key = { _, item -> item.itemId },
                    ) { index, uiModel ->
                        Column(modifier = Modifier.animateItem()) {
                            SearchListItem(
                                modifier = Modifier.fillMaxWidth(),
                                uiModel = uiModel,
                                onIntent = onIntentWithDismiss,
                            )

                            if (index < state.books.lastIndex) {
                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = LeafTheme.colors.border,
                                )
                            }
                        }
                    }

                    item {
                        if (paging.isLoadingFooter) {
                            PagingLoadingFooter()
                        }
                    }

                    item {
                        if (paging.isErrorFooter) {
                            PagingErrorFooter(
                                onRefresh = { onIntent(SearchIntent.RefreshClick) },
                            )
                        }
                    }
                }
            }

            FadeAnimatedVisibility(paging.isSuccess && state.books.isEmpty()) {
                SearchEmptyContent(
                    keyword = paging.keyword,
                )
            }
        }
    }

    if (state.isShowBookDetailDialog && state.selectedBook != null) {
        SearchBookDetailBottomSheet(
            uiModel = state.selectedBook,
            onIntent = onIntent,
        )
    }
}

@ThemePreviews
@Composable
private fun SearchScreenSuccessPreview() {
    LeafTheme {
        val mocks =
            persistentListOf(
                SearchBookUiModel.Mock,
                SearchBookUiModel.Mock.copy(
                    itemId = 2L,
                    title = "불안의 서",
                    author = "페르난두 페소아",
                    publisher = "봄날의 책",
                ),
                SearchBookUiModel.Mock.copy(
                    itemId = 3L,
                    title = "초콜릿 우유 대신 청산가리를 마시는 방법",
                    author = "박정민",
                    publisher = "위즈덤하우스",
                ),
            )

        SearchScreen(
            state =
                SearchState(
                    pagingState = PagingState(keyword = "페소아", isLast = true),
                    books = mocks,
                ),
            onIntent = {},
        )
    }
}
