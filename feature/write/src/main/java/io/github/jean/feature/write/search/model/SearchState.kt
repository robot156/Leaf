package io.github.jean.feature.write.search.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import io.github.jean.feature.write.search.model.section.SearchBookUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class SearchState(
    val searchTextState: TextFieldState = TextFieldState(),
    val pagingState: PagingState = PagingState(),
    val books: ImmutableList<SearchBookUiModel> = persistentListOf(),
    val selectedBook: SearchBookUiModel? = null,
    val isShowBookDetailDialog: Boolean = false,
)

@Stable
data class PagingState(
    val keyword: String = "",
    val isLoading: Boolean = false,
    val isLoadingFooter: Boolean = false,
    val isError: Boolean = false,
    val isErrorFooter: Boolean = false,
    val isLast: Boolean = false,
    val nextPage: Int = 1,
) {
    val isSuccess: Boolean
        get() = !isLoading && !isError
}
