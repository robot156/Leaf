package io.github.jean.feature.write.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafChip
import io.github.jean.core.designsystem.component.LeafDatePickerDialog
import io.github.jean.core.designsystem.component.LeafOutlinedButton
import io.github.jean.core.designsystem.component.LeafTopNavigation
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.write.editor.model.EditorIntent
import io.github.jean.feature.write.editor.model.EditorSideEffect
import io.github.jean.feature.write.editor.model.EditorState
import io.github.jean.feature.write.editor.model.section.EditorBlock
import io.github.jean.feature.write.editor.model.section.EditorBookUiModel
import io.github.jean.feature.write.editor.ui.EditorBookCard
import io.github.jean.feature.write.editor.ui.EditorBottomBar
import io.github.jean.feature.write.editor.ui.EditorQuoteBlockField
import io.github.jean.feature.write.editor.ui.EditorRecordBlockField
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun EditorRoute(
    noteId: Long?,
    navigateToBack: () -> Unit,
    navigateToDetail: (noteId: Long) -> Unit,
    navigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditorViewModel = assistedMetroViewModel<EditorViewModel, EditorViewModel.Factory> { create(noteId) },
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is EditorSideEffect.NavigateToBack -> {
                navigateToBack()
            }

            is EditorSideEffect.NavigateToSearch -> {
                navigateToSearch()
            }

            is EditorSideEffect.NavigateToDetail -> {
                navigateToDetail(effect.noteId)
            }
        }
    }

    EditorScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun EditorScreen(
    state: EditorState,
    onIntent: (EditorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier =
            modifier
                .background(LeafTheme.colors.surface)
                .systemBarsPadding()
                .imePadding(),
        topBar = {
            LeafTopNavigation(
                title = stringResource(if (state.noteId == null) R.string.editor_new_note else R.string.editor_note),
                leading =
                    LeafTopNavItem.Icon(
                        iconRes = LeafTheme.res.chevronLeft,
                        onClick = { onIntent(EditorIntent.BackClick) },
                    ),
                trailing =
                    persistentListOf(
                        LeafTopNavItem.TextButton(
                            text = stringResource(R.string.editor_save),
                            onClick = { onIntent(EditorIntent.SaveClick) },
                            enabled = state.isSaveEnable,
                        ),
                    ),
            )
        },
        content = {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(12.dp),
            ) {
                Spacer(Modifier.height(10.dp))

                if (state.book == null) {
                    LeafOutlinedButton(
                        text = stringResource(R.string.editor_book_choice),
                        leadingIconRes = LeafTheme.res.plus,
                        fillMaxWidth = true,
                        onClick = { onIntent(EditorIntent.BookChoiceClick) },
                    )
                } else {
                    EditorBookCard(
                        book = state.book,
                        onIntent = onIntent,
                    )
                }

                LeafChip(
                    modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                    text = state.noteDateText,
                    trailingIconRes = LeafTheme.res.chevronDown,
                    onClick = { onIntent(EditorIntent.DateChipClick) },
                )

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 14.dp),
                ) {
                    state.quotes.forEach { quote ->
                        key(quote.id) {
                            EditorQuoteBlockField(
                                block = quote,
                                requestFocus = state.focusedBlockId == quote.id,
                                onIntent = onIntent,
                            )
                        }
                    }

                    EditorRecordBlockField(
                        state = state.recordState,
                        showPlaceholder = state.quotes.isEmpty(),
                        requestFocus = state.focusedBlockId == EditorState.RECORD_BLOCK_ID,
                    )
                }
            }
        },
        bottomBar = {
            EditorBottomBar(
                hint = stringResource(R.string.editor_char_count, state.wordCount),
                onIntent = onIntent,
            )
        },
    )

    if (state.isShowDatePicker) {
        LeafDatePickerDialog(
            initialDate = state.noteDate,
            onConfirm = { onIntent(EditorIntent.DatePickerConfirm(it)) },
            onDismiss = { onIntent(EditorIntent.DatePickerDialogDismiss) },
        )
    }
}

@ThemePreviews
@Composable
private fun EditorScreenPreview() {
    LeafTheme {
        EditorScreen(
            state =
                EditorState(
                    noteDateText = "2026년 7월 12일",
                    book =
                        EditorBookUiModel(
                            bookId = 0,
                            title = "불안의 서",
                            author = "페르난두 페소아",
                            coverUrl = "",
                        ),
                    quotes =
                        persistentListOf(
                            EditorBlock.Quote(id = 1, page = 15, sentence = "나는 내 안의 여럿과 함께 걷는다."),
                        ),
                    recordState = TextFieldState("여럿으로 사는 일이 그에게는 불안이 아니라 존재 방식이었다."),
                ),
            onIntent = {},
        )
    }
}
