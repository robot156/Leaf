package io.github.jean.feature.home.model.section

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.BookNoteWithBook
import io.github.jean.core.common.time.LeafDateFormat

@Immutable
data class HomeBookUiModel(
    val noteId: Long,
    val title: String,
    val author: String,
    val lastEditedDate: String,
)

fun BookNoteWithBook.toUiModel(): HomeBookUiModel =
    HomeBookUiModel(
        noteId = note.id,
        title = book.title,
        author = book.author,
        lastEditedDate = LeafDateFormat.display(note.createdAt),
    )
