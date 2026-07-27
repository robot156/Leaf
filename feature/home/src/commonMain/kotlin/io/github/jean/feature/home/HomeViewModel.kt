package io.github.jean.feature.home

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.jean.core.data.note.NoteRepository
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.home.model.HomeIntent
import io.github.jean.feature.home.model.HomeSideEffect
import io.github.jean.feature.home.model.HomeState
import io.github.jean.feature.home.model.section.toUiModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.orbitmvi.orbit.syntax.Syntax

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
class HomeViewModel(
    private val noteRepository: NoteRepository,
) : MVIViewModel<HomeState, HomeSideEffect>(HomeState()) {
    private val invalidateCount = MutableStateFlow(0)

    override suspend fun Syntax<HomeState, HomeSideEffect>.onContainerCreate() {
        repeatOnSubscription {
            invalidateCount
                .flatMapLatest {
                    noteRepository
                        .getNotes()
                        .map { notes -> notes.map { it.toUiModel() } }
                        .catch { reduce { state.copy(isLoading = false, isError = true) } }
                }.collect { uiModels ->
                    reduce {
                        state.copy(
                            isLoading = false,
                            isError = false,
                            bookNotes = uiModels.toPersistentList(),
                        )
                    }
                }
        }
    }

    override fun onIntent(intent: Intent) {
        if (intent !is HomeIntent) {
            super.onIntent(intent)
            return
        }

        intent {
            when (intent) {
                is HomeIntent.RefreshClick -> {
                    if (state.isError) {
                        reduce { state.copy(isLoading = true) }
                    }
                    invalidateCount.update { it + 1 }
                }

                is HomeIntent.AddClick -> {
                    postSideEffect(HomeSideEffect.NavigateToEditor)
                }

                is HomeIntent.BookNoteClick -> {
                    postSideEffect(HomeSideEffect.NavigateToDetail(intent.noteId))
                }

                is HomeIntent.MenuClick -> {
                    postSideEffect(HomeSideEffect.NavigateToSetting)
                }
            }
        }
    }
}
