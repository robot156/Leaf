package io.github.jean.feature.write.editor.model

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.NoteContent
import kotlinx.datetime.LocalDate

/**
 * 저장하지 않은 변경이 있는지 판단하기 위한 에디터 내용 스냅샷.
 *
 * 블록 id는 세션마다 새로 발급되므로 비교에서 제외하고,
 * 실제 저장되는 형태([NoteContent])로 환산해서 비교한다.
 */
@Immutable
data class EditorSnapshot(
    val bookId: Long?,
    val noteDate: LocalDate,
    val content: List<NoteContent>,
)
