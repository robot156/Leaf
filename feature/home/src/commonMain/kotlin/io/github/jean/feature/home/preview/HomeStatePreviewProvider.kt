package io.github.jean.feature.home.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.jean.feature.home.model.HomeState
import io.github.jean.feature.home.model.section.HomeBookUiModel
import kotlinx.collections.immutable.persistentListOf

class HomeStatePreviewProvider : PreviewParameterProvider<HomeState> {
    override val values: Sequence<HomeState> =
        sequenceOf(
            HomeState(isLoading = true),
            HomeState(isLoading = false, isError = true),
            HomeState(isLoading = false),
            HomeState(isLoading = false, bookNotes = mockBookNotes),
        )

    override fun getDisplayName(index: Int): String? =
        when (index) {
            0 -> "Loading"
            1 -> "Error"
            2 -> "Empty"
            3 -> "List"
            else -> super.getDisplayName(index)
        }
}

private val mockBookNotes =
    persistentListOf(
        HomeBookUiModel(
            noteId = 1L,
            title = "불안의 서",
            author = "페르난두 페소아",
            lastEditedDate = "2026.06.28",
        ),
        HomeBookUiModel(
            noteId = 2L,
            title = "알려지지 않은 예술가의 눈물과 자이툰 파스타",
            author = "박상영",
            lastEditedDate = "2026.06.28",
        ),
    )
