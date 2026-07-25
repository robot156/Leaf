package io.github.jean.core.data.note

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.model.BookNote
import io.github.jean.core.common.model.BookNoteWithBook
import io.github.jean.core.datalocal.datasource.note.BookNoteLocalDataSource
import kotlinx.coroutines.flow.Flow

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class NoteRepositoryImpl(
    private val bookNoteLocalDataSource: BookNoteLocalDataSource,
) : NoteRepository {
    override fun getNotes(): Flow<List<BookNoteWithBook>> = bookNoteLocalDataSource.observeAllNotesWithBook()

    override suspend fun getNote(noteId: Long): BookNote =
        bookNoteLocalDataSource.getNote(noteId)
            ?: error("BookNote(id=$noteId) not found")

    override suspend fun getNoteWithBook(noteId: Long): BookNoteWithBook =
        bookNoteLocalDataSource.getNoteWithBook(noteId)
            ?: error("BookNote(id=$noteId) not found")

    override fun getNoteWithBookAtFlow(noteId: Long): Flow<BookNoteWithBook?> =
        bookNoteLocalDataSource.observeNoteWithBook(noteId)

    override suspend fun saveNote(bookNote: BookNote): Long = bookNoteLocalDataSource.saveNote(bookNote)

    override suspend fun updateNote(bookNote: BookNote): BookNote {
        bookNoteLocalDataSource.updateNote(bookNote)
        return bookNote
    }

    override suspend fun deleteNote(noteId: Long) {
        bookNoteLocalDataSource.deleteNote(noteId)
    }
}
