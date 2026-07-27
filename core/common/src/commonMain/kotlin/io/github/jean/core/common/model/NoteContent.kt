package io.github.jean.core.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface NoteContent {
    @Serializable
    @SerialName("quote")
    data class Quote(
        val page: Int?,
        val sentence: String,
    ) : NoteContent

    @Serializable
    @SerialName("record")
    data class Record(
        val content: String,
    ) : NoteContent
}
