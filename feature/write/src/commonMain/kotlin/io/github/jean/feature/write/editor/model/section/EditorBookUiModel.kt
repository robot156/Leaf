package io.github.jean.feature.write.editor.model.section

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.Book

@Immutable
data class EditorBookUiModel(
    val bookId: Long,
    val title: String,
    val author: String,
    val coverUrl: String?,
)

fun Book.toUiModel(): EditorBookUiModel =
    EditorBookUiModel(
        bookId = itemId,
        title = title,
        author = author,
        coverUrl = cover.ifBlank { null },
    )
