package io.github.jean.feature.write.editor

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import io.github.jean.core.common.eventbus.BookSelectedAction
import io.github.jean.core.common.eventbus.EventBus
import io.github.jean.core.common.model.BookNote
import io.github.jean.core.common.time.LeafDateFormat
import io.github.jean.core.common.time.now
import io.github.jean.core.common.time.toInstant
import io.github.jean.core.common.time.toLocalDate
import io.github.jean.core.data.note.NoteRepository
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.write.editor.model.EditorIntent
import io.github.jean.feature.write.editor.model.EditorSideEffect
import io.github.jean.feature.write.editor.model.EditorState
import io.github.jean.feature.write.editor.model.section.EditorBlock
import io.github.jean.feature.write.editor.model.section.toEditorBlock
import io.github.jean.feature.write.editor.model.section.toNoteContent
import io.github.jean.feature.write.editor.model.section.toUiModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.Syntax

@AssistedInject
class EditorViewModel(
    @Assisted private val noteId: Long?,
    private val noteRepository: NoteRepository,
    eventBus: EventBus<BookSelectedAction>,
) : MVIViewModel<EditorState, EditorSideEffect>(EditorState()) {
    private var blockIdCounter = 1L

    init {
        viewModelScope.launch {
            eventBus.action.collect { action ->
                when (action) {
                    is BookSelectedAction.BookSelected -> {
                        intent {
                            reduce { state.copy(book = action.book.toUiModel()) }
                        }
                    }
                }
            }
        }
    }

    override suspend fun Syntax<EditorState, EditorSideEffect>.onContainerCreate() {
        if (noteId == null) return

        val (note, book) = noteRepository.getNoteWithBook(noteId)
        val items =
            note
                .content
                .mapIndexed { index, noteContent ->
                    noteContent.toEditorBlock(index.toLong())
                }
        val recordContent =
            items
                .filterIsInstance<EditorBlock.Record>()
                .firstOrNull()
                ?.content
                .orEmpty()
        state.recordState.setTextAndPlaceCursorAtEnd(recordContent)

        reduce {
            state.copy(
                noteId = note.id,
                noteDate = note.createdAt.toLocalDate(),
                noteDateText = LeafDateFormat.displayVerbose(note.createdAt),
                book = book.toUiModel(),
                quotes = items.filterIsInstance<EditorBlock.Quote>().toPersistentList(),
            )
        }
    }

    override fun onIntent(intent: Intent) {
        if (intent !is EditorIntent) {
            super.onIntent(intent)
            return
        }

        intent {
            when (intent) {
                is EditorIntent.BackClick -> {
                    postSideEffect(EditorSideEffect.NavigateToBack)
                }

                is EditorIntent.SaveClick -> {
                    saveNote()
                }

                is EditorIntent.BookChoiceClick -> {
                    postSideEffect(EditorSideEffect.NavigateToSearch)
                }

                is EditorIntent.DateChipClick -> {
                    reduce {
                        state.copy(isShowDatePicker = true)
                    }
                }

                is EditorIntent.DatePickerConfirm -> {
                    reduce {
                        state.copy(
                            noteDate = intent.localDate,
                            noteDateText = LeafDateFormat.displayVerbose(intent.localDate.toInstant()),
                            isShowDatePicker = false,
                        )
                    }
                }

                is EditorIntent.DatePickerDialogDismiss -> {
                    reduce {
                        state.copy(isShowDatePicker = false)
                    }
                }

                is EditorIntent.QuoteAddClick -> {
                    addQuote()
                }

                is EditorIntent.QuoteSentenceChanged -> {
                    updateQuote(intent.blockId) { it.copy(sentence = intent.sentence) }
                }

                is EditorIntent.QuotePageChanged -> {
                    updateQuote(intent.blockId) { it.copy(page = intent.page) }
                }

                is EditorIntent.QuoteRemoveClick -> {
                    removeQuote(intent.blockId)
                }
            }
        }
    }

    private fun saveNote() {
        intent {
            val bookId = state.book?.bookId ?: return@intent

            val recordBlock =
                EditorBlock.Record(
                    id = EditorState.RECORD_BLOCK_ID,
                    content = state.recordState.text.toString(),
                )
            // 화면 순서와 동일하게 인용들 뒤에 본문을 붙인다. 빈 블록은 매퍼가 걸러낸다.
            val content = (state.quotes + recordBlock).toNoteContent()

            val currentNoteId = state.noteId
            val note =
                BookNote(
                    id = currentNoteId ?: 0L,
                    bookId = bookId,
                    content = content,
                    createdAt = state.noteDate.toInstant(),
                    updatedAt = now(),
                )
            if (!note.isSavable) return@intent

            if (currentNoteId == null) {
                // 신규 저장은 방금 만든 기록의 상세로 이동한다 (에디터는 스택에서 대체됨)
                val savedNoteId = noteRepository.saveNote(note)
                postSideEffect(EditorSideEffect.NavigateToDetail(savedNoteId))
            } else {
                // 수정은 진입했던 상세 화면으로 뒤로 돌아간다
                noteRepository.updateNote(note)
                postSideEffect(EditorSideEffect.NavigateToBack)
            }
        }
    }

    private fun addQuote() {
        intent {
            val quote = EditorBlock.Quote(id = createBlockId())
            val quotes =
                state.quotes
                    .toMutableList()
                    .apply { add(quote) }
                    .toPersistentList()

            reduce {
                state.copy(
                    quotes = quotes,
                    focusedBlockId = quote.id,
                )
            }
        }
    }

    private fun removeQuote(blockId: Long) {
        intent {
            val quotes = state.quotes.filterNot { it.id == blockId }.toPersistentList()
            reduce { state.copy(quotes = quotes) }
        }
    }

    private fun updateQuote(
        blockId: Long,
        transform: (EditorBlock.Quote) -> EditorBlock.Quote,
    ) {
        intent {
            val quotes =
                state.quotes
                    .map { if (it.id == blockId) transform(it) else it }
                    .toPersistentList()

            reduce { state.copy(quotes = quotes) }
        }
    }

    private fun createBlockId(): Long = blockIdCounter++

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(
            @Assisted noteId: Long?,
        ): EditorViewModel
    }
}
