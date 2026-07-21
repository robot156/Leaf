package io.github.jean.feature.note.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafAlertDialog
import io.github.jean.core.designsystem.component.LeafTopNavigation
import io.github.jean.core.designsystem.component.node.LeafDialogButton
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.note.detail.model.NoteDetailIntent
import io.github.jean.feature.note.detail.model.NoteDetailSideEffect
import io.github.jean.feature.note.detail.model.NoteDetailState
import io.github.jean.feature.note.detail.model.section.NoteDetailBlockUiModel
import io.github.jean.feature.note.detail.model.section.NoteDetailBookUiModel
import io.github.jean.feature.note.detail.ui.NoteDetailBookDetailBottomSheet
import io.github.jean.feature.note.detail.ui.NoteDetailBookHeader
import io.github.jean.feature.note.detail.ui.NoteDetailQuoteBlock
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun NoteDetailRoute(
    noteId: Long,
    navigateToBack: () -> Unit,
    navigateToEditor: (noteId: Long) -> Unit,
    navigateToExternalWeb: (link: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteDetailViewModel =
        assistedMetroViewModel<NoteDetailViewModel, NoteDetailViewModel.Factory> {
            create(noteId)
        },
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is NoteDetailSideEffect.NavigateToBack -> {
                navigateToBack()
            }

            is NoteDetailSideEffect.NavigateToEditor -> {
                navigateToEditor(effect.noteId)
            }

            is NoteDetailSideEffect.NavigateToExternalWeb -> {
                navigateToExternalWeb(effect.link)
            }
        }
    }

    NoteDetailScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun NoteDetailScreen(
    state: NoteDetailState,
    onIntent: (NoteDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(LeafTheme.colors.surface)
                .systemBarsPadding(),
    ) {
        LeafTopNavigation(
            title = stringResource(R.string.note_detail_title),
            leading =
                LeafTopNavItem.Icon(
                    iconRes = LeafTheme.res.chevronLeft,
                    onClick = { onIntent(NoteDetailIntent.BackClick) },
                ),
            trailing =
                persistentListOf(
                    LeafTopNavItem.TextButton(
                        text = stringResource(R.string.note_detail_delete),
                        onClick = { onIntent(NoteDetailIntent.DeleteClick) },
                    ),
                    LeafTopNavItem.TextButton(
                        text = stringResource(R.string.note_detail_edit),
                        onClick = { onIntent(NoteDetailIntent.EditClick) },
                    ),
                ),
        )

        NoteDetailBookHeader(
            title = state.book.title,
            author = state.book.author,
            coverUrl = state.book.coverUrl,
            recordedDateText = stringResource(R.string.note_detail_recorded_date, state.recordedDate),
            onClick = { onIntent(NoteDetailIntent.BookHeaderClick) },
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 20.dp),
        ) {
            state.blocks.forEach { block ->
                when (block) {
                    is NoteDetailBlockUiModel.Quote -> {
                        NoteDetailQuoteBlock(
                            page = block.page,
                            sentence = block.sentence,
                            modifier = Modifier.padding(bottom = 18.dp),
                        )
                    }

                    is NoteDetailBlockUiModel.Record -> {
                        Text(
                            modifier =
                                modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 18.dp),
                            text = block.content,
                            style = LeafTheme.typography.note,
                            color = LeafTheme.colors.textPrimary,
                        )
                    }
                }
            }
        }
    }

    if (state.isShowBookDetailDialog) {
        NoteDetailBookDetailBottomSheet(
            uiModel = state.book,
            onIntent = onIntent,
        )
    }

    if (state.isShowEditDialog) {
        LeafAlertDialog(
            onDismiss = { onIntent(NoteDetailIntent.DeleteDialogDismiss) },
            title = stringResource(R.string.note_detail_delete_dialog_title),
            description = stringResource(R.string.note_detail_delete_dialog_description),
            negativeButton =
                LeafDialogButton(
                    text = stringResource(R.string.note_detail_cancel),
                    onClick = { onIntent(NoteDetailIntent.CancelClick) },
                ),
            positiveButton =
                LeafDialogButton(
                    text = stringResource(R.string.note_detail_delete),
                    onClick = { onIntent(NoteDetailIntent.DeleteConfirmClick) },
                ),
        )
    }
}

@ThemePreviews
@Composable
private fun NoteDetailScreenPreview() {
    val state =
        NoteDetailState(
            noteId = 1L,
            book = NoteDetailBookUiModel.Mock,
            recordedDate = "2026년 07월 12일",
            blocks =
                persistentListOf(
                    NoteDetailBlockUiModel.Quote(
                        page = 15,
                        sentence = "나는 내 안의 여럿과 함께 걷는다. 내가 나일 때조차, 나는 그들 중 하나일 뿐이다.",
                    ),
                    NoteDetailBlockUiModel.Record(
                        content =
                            "여럿으로 사는 일이 그에게는 불안이 아니라 존재 방식이었다. " +
                                "회사에서의 나, 집에서의 나, 글을 쓸 때의 나 — 그 사이의 간격을 메우려 " +
                                "애쓰지 않아도 된다는 말로 읽혔다. 오늘은 그 문장 앞에서 오래 멈춰 있었다.",
                    ),
                ),
        )

    LeafTheme {
        NoteDetailScreen(
            state = state,
            onIntent = {},
        )
    }
}
