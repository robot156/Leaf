package io.github.jean.feature.write.editor.model.section

import io.github.jean.core.common.model.NoteContent

fun NoteContent.toEditorBlock(id: Long): EditorBlock =
    when (this) {
        is NoteContent.Record -> EditorBlock.Record(id = id, content = content)
        is NoteContent.Quote -> EditorBlock.Quote(id = id, page = page, sentence = sentence)
    }

/** 빈 블록은 저장 대상에서 제외한다 */
fun List<EditorBlock>.toNoteContent(): List<NoteContent> =
    mapNotNull { block ->
        when (block) {
            is EditorBlock.Record ->
                block.content.takeIf { it.isNotBlank() }?.let { NoteContent.Record(it) }

            is EditorBlock.Quote ->
                block.sentence.takeIf { it.isNotBlank() }?.let { NoteContent.Quote(block.page, it) }
        }
    }
