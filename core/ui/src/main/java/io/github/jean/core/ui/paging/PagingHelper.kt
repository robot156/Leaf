package io.github.jean.core.ui.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun PaginationEffect(
    listState: LazyListState,
    threshold: Int = 3,
    enabled: Boolean = true,
    onNext: () -> Unit,
) {
    val callback by rememberUpdatedState(onNext)
    val shouldLoadNextPage by remember(listState, threshold) {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - threshold
        }
    }

    LaunchedEffect(shouldLoadNextPage, enabled) {
        if (shouldLoadNextPage && enabled) {
            callback()
        }
    }
}
