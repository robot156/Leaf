package io.github.jean.feature.note.detail

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import io.github.jean.core.common.time.LeafDateFormat
import io.github.jean.core.data.note.NoteRepository
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.note.detail.model.NoteDetailIntent
import io.github.jean.feature.note.detail.model.NoteDetailSideEffect
import io.github.jean.feature.note.detail.model.NoteDetailState
import io.github.jean.feature.note.detail.model.section.toUiModel
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.syntax.Syntax

@AssistedInject
class NoteDetailViewModel(
    @Assisted private val noteId: Long,
    private val noteRepository: NoteRepository,
) : MVIViewModel<NoteDetailState, NoteDetailSideEffect>(NoteDetailState()) {
    override suspend fun Syntax<NoteDetailState, NoteDetailSideEffect>.onContainerCreate() {
        noteRepository.getNoteWithBookAtFlow(noteId).collect { noteWithBook ->
            if (noteWithBook == null) {
                postSideEffect(NoteDetailSideEffect.NavigateToBack)
                return@collect
            }

            val (note, book) = noteWithBook

            reduce {
                state.copy(
                    noteId = note.id,
                    book = book.toUiModel(),
                    recordedDate = LeafDateFormat.displayVerbose(note.createdAt),
                    blocks = note.content.map { it.toUiModel() }.toPersistentList(),
                )
            }
        }
    }

    override fun onIntent(intent: Intent) {
        if (intent !is NoteDetailIntent) {
            super.onIntent(intent)
            return
        }

        intent {
            when (intent) {
                is NoteDetailIntent.BackClick -> {
                    postSideEffect(NoteDetailSideEffect.NavigateToBack)
                }

                is NoteDetailIntent.EditClick -> {
                    postSideEffect(NoteDetailSideEffect.NavigateToEditor(state.noteId))
                }

                is NoteDetailIntent.CancelClick -> {
                    reduce { state.copy(isShowEditDialog = false) }
                }

                is NoteDetailIntent.DeleteClick -> {
                    reduce { state.copy(isShowEditDialog = true) }
                }

                is NoteDetailIntent.ShareClick -> {
                    reduce { state.copy(isShared = true) }
                }

                is NoteDetailIntent.RecordShare -> {
                    reduce { state.copy(isShared = false) }
                    postSideEffect(NoteDetailSideEffect.NavigateToShareRecord(intent.bitmap))
                }

                is NoteDetailIntent.DeleteConfirmClick -> {
                    reduce { state.copy(isShowEditDialog = false) }
                    noteRepository.deleteNote(noteId)
                }

                is NoteDetailIntent.DeleteDialogDismiss -> {
                    reduce { state.copy(isShowEditDialog = false) }
                }

                is NoteDetailIntent.BookHeaderClick -> {
                    reduce { state.copy(isShowBookDetailDialog = true) }
                }

                is NoteDetailIntent.BookDetailDialogHide -> {
                    reduce { state.copy(isShowBookDetailDialog = false) }
                }

                is NoteDetailIntent.BookCoverImageClick -> {
                    reduce { state.copy(isShowBookDetailDialog = false) }
                    postSideEffect(NoteDetailSideEffect.NavigateToImageViewer(intent.imageUrl))
                }

                is NoteDetailIntent.ExternalWebSiteClick -> {
                    postSideEffect(NoteDetailSideEffect.NavigateToExternalWeb(intent.webLink))
                }
            }
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(
            @Assisted noteId: Long,
        ): NoteDetailViewModel
    }
}
