package io.github.jean.feature.write.editor.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import io.github.jean.core.common.time.LeafDateFormat
import io.github.jean.core.common.time.now
import io.github.jean.feature.write.editor.model.section.EditorBlock
import io.github.jean.feature.write.editor.model.section.EditorBookUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

@Stable
data class EditorState(
    val noteId: Long? = null,
    val noteDate: LocalDate = LocalDate.now(),
    val noteDateText: String = LeafDateFormat.displayVerbose(now()),
    val book: EditorBookUiModel? = null,
    val quotes: ImmutableList<EditorBlock.Quote> = persistentListOf(),
    // 본문은 TextFieldState가 단일 소스. 키 입력은 이 홀더가 직접 관리(intent 왕복 없음).
    val recordState: TextFieldState = TextFieldState(),
    val focusedBlockId: Long? = null,
    val isShowDatePicker: Boolean = false,
) {
    val wordCount: Int
        get() = recordState.text.length + quotes.sumOf { it.sentence.length }

    // 책이 선택됐고 본문/인용 중 하나라도 내용이 있어야 저장 가능.
    // recordState.text는 스냅샷 상태라 이 값을 읽는 Composable이 입력에 맞춰 갱신된다.
    val isSaveEnable: Boolean
        get() = book != null && (recordState.text.isNotBlank() || quotes.any { it.sentence.isNotBlank() })

    companion object {
        const val RECORD_BLOCK_ID = 0L
    }
}
