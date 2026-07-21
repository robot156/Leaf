package io.github.jean.core.data.note

import io.github.jean.core.common.model.BookNote
import io.github.jean.core.common.model.BookNoteWithBook
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<BookNoteWithBook>>

    suspend fun getNote(noteId: Long): BookNote

    suspend fun getNoteWithBook(noteId: Long): BookNoteWithBook

    fun getNoteWithBookAtFlow(noteId: Long): Flow<BookNoteWithBook?>

    suspend fun saveNote(bookNote: BookNote): Long

    suspend fun updateNote(bookNote: BookNote): BookNote

    suspend fun deleteNote(noteId: Long)
}
