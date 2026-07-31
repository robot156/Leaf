package io.github.jean.feature.write.editor.model.section

import androidx.compose.runtime.Immutable

sealed interface EditorBlock {
    val id: Long

    @Immutable
    data class Record(
        override val id: Long,
        val content: String = "",
    ) : EditorBlock

    @Immutable
    data class Quote(
        override val id: Long,
        val page: Int? = null,
        val sentence: String = "",
    ) : EditorBlock
}
