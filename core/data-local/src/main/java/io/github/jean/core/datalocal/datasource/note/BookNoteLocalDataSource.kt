package io.github.jean.core.datalocal.datasource.note

import io.github.jean.core.common.model.BookNote
import io.github.jean.core.common.model.BookNoteWithBook
import kotlinx.coroutines.flow.Flow

interface BookNoteLocalDataSource {
    fun observeAllNotesWithBook(): Flow<List<BookNoteWithBook>>

    suspend fun getNote(id: Long): BookNote?

    suspend fun getNoteWithBook(id: Long): BookNoteWithBook?

    fun observeNoteWithBook(id: Long): Flow<BookNoteWithBook?>

    suspend fun saveNote(note: BookNote): Long

    suspend fun updateNote(note: BookNote)

    suspend fun deleteNote(note: BookNote)

    suspend fun deleteNote(id: Long)

    suspend fun deleteByBook(bookId: Long)
}
