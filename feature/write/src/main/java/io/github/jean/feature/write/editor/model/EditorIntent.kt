package io.github.jean.feature.write.editor.model

import io.github.jean.core.ui.mvi.Intent
import kotlinx.datetime.LocalDate

sealed interface EditorIntent : Intent {
    data object BackClick : EditorIntent

    data object SaveClick : EditorIntent

    data object BookChoiceClick : EditorIntent

    data object QuoteAddClick : EditorIntent

    data object DateChipClick : EditorIntent

    data class QuoteSentenceChanged(
        val blockId: Long,
        val sentence: String,
    ) : EditorIntent

    data class QuotePageChanged(
        val blockId: Long,
        val page: Int?,
    ) : EditorIntent

    data class QuoteRemoveClick(
        val blockId: Long,
    ) : EditorIntent

    data class DatePickerConfirm(
        val localDate: LocalDate,
    ) : EditorIntent

    data object DatePickerDialogDismiss : EditorIntent
}
