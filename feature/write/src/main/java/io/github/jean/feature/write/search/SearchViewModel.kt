package io.github.jean.feature.write.search

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.jean.core.common.eventbus.BookSelectedAction
import io.github.jean.core.common.eventbus.EventBus
import io.github.jean.core.common.model.BookContainer
import io.github.jean.core.data.book.BookRepository
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.write.search.model.SearchIntent
import io.github.jean.feature.write.search.model.SearchSideEffect
import io.github.jean.feature.write.search.model.SearchState
import io.github.jean.feature.write.search.model.section.SearchBookUiModel
import io.github.jean.feature.write.search.model.section.toBook
import io.github.jean.feature.write.search.model.section.toUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.syntax.Syntax

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
class SearchViewModel(
    private val bookRepository: BookRepository,
    private val eventBus: EventBus<BookSelectedAction>,
) : MVIViewModel<SearchState, SearchSideEffect>(SearchState()) {
    override fun onIntent(intent: Intent) {
        if (intent !is SearchIntent) {
            super.onIntent(intent)
            return
        }
        intent {
            when (intent) {
                is SearchIntent.BackClick -> {
                    postSideEffect(SearchSideEffect.NavigateToBack)
                }

                is SearchIntent.RefreshClick -> {
                    getSearchBook(keyword = state.pagingState.keyword, isNewLoad = false)
                }

                is SearchIntent.SearchClick -> {
                    val keyword =
                        state.searchTextState.text
                            .toString()
                            .trim()
                    getSearchBook(keyword = keyword, isNewLoad = true)
                }

                is SearchIntent.NextPageLoad -> {
                    getSearchBook(keyword = state.pagingState.keyword, isNewLoad = false)
                }

                is SearchIntent.BookClick -> {
                    selectBook(intent.book)
                }

                is SearchIntent.BookDetailClick -> {
                    setDetailBookShow(isShow = true, selectedBook = intent.book)
                }

                is SearchIntent.BookDetailDialogHide -> {
                    setDetailBookShow(isShow = false)
                }

                is SearchIntent.ExternalWebSiteClick -> {
                    postSideEffect(SearchSideEffect.NavigateToExternalWeb(intent.webLink))
                }
            }
        }
    }

    private fun selectBook(book: SearchBookUiModel) {
        intent {
            setDetailBookShow(false, null)
            eventBus.send(BookSelectedAction.BookSelected(book.toBook()))
            postSideEffect(SearchSideEffect.NavigateToBack)
        }
    }

    private fun getSearchBook(
        keyword: String,
        isNewLoad: Boolean,
    ) {
        intent {
            val page = applyLoading(keyword, isNewLoad)

            runCatching {
                bookRepository.getSearchBooks(
                    query = keyword,
                    page = page,
                    pageSize = PAGE_SIZE,
                )
            }.onFailure {
                applyError(keyword)
            }.onSuccess { bookContainer ->
                applySuccess(keyword, bookContainer)
            }
        }
    }

    /** 로딩 상태로 전환하고 이번에 요청할 페이지를 반환한다. */
    private suspend fun Syntax<SearchState, SearchSideEffect>.applyLoading(
        keyword: String,
        isNewLoad: Boolean,
    ): Int {
        val current = state.pagingState
        val page = if (isNewLoad) 1 else current.nextPage

        reduce {
            state.copy(
                pagingState =
                    current.copy(
                        keyword = keyword,
                        isLoading = isNewLoad || page == 1,
                        isLoadingFooter = !isNewLoad && page > 1,
                        isError = false,
                        isErrorFooter = false,
                        nextPage = page,
                    ),
                books = if (isNewLoad) persistentListOf() else state.books,
            )
        }

        return page
    }

    private suspend fun Syntax<SearchState, SearchSideEffect>.applyError(requestKeyword: String) {
        // 키워드가 바뀐 뒤 도착한 이전 요청의 응답은 무시한다
        if (requestKeyword != state.pagingState.keyword) return

        reduce {
            val pageState = state.pagingState
            state.copy(
                pagingState =
                    pageState.copy(
                        isLoadingFooter = false,
                        isLoading = false,
                        isError = pageState.nextPage == 1,
                        isErrorFooter = pageState.nextPage > 1,
                    ),
            )
        }
    }

    private suspend fun Syntax<SearchState, SearchSideEffect>.applySuccess(
        requestKeyword: String,
        bookContainer: BookContainer,
    ) {
        if (requestKeyword != state.pagingState.keyword) return

        val pageState = state.pagingState
        // API에서 없는 페이지를 요청할 경우 1번 페이지 응답값을 준다. 해당 케이스 대응
        val isOver = pageState.nextPage > bookContainer.startIndex
        if (isOver) {
            reduce {
                state.copy(
                    pagingState =
                        pageState.copy(
                            isLoadingFooter = false,
                            isLoading = false,
                            isError = false,
                            isErrorFooter = false,
                            isLast = true,
                        ),
                )
            }

            return
        }

        val uiModels = bookContainer.books.map { it.toUiModel() }
        val loadedCount = state.books.size + uiModels.size

        reduce {
            state.copy(
                pagingState =
                    pageState.copy(
                        isLoadingFooter = false,
                        isLoading = false,
                        isError = false,
                        isErrorFooter = false,
                        isLast = loadedCount >= bookContainer.totalResults || bookContainer.books.isEmpty(),
                        nextPage = pageState.nextPage.plus(1),
                    ),
                books = (state.books + uiModels).distinctBy { it.itemId }.toPersistentList(),
            )
        }
    }

    private fun setDetailBookShow(
        isShow: Boolean,
        selectedBook: SearchBookUiModel? = null,
    ) {
        intent {
            reduce {
                state.copy(
                    isShowBookDetailDialog = isShow,
                    selectedBook = selectedBook,
                )
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}
