package io.github.jean.core.datalocal.datasource.note

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.model.BookNote
import io.github.jean.core.common.model.BookNoteWithBook
import io.github.jean.core.datalocal.database.dao.BookNoteDao
import io.github.jean.core.datalocal.database.entity.BookNoteWithBookEntity
import io.github.jean.core.datalocal.database.entity.toDomain
import io.github.jean.core.datalocal.database.entity.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class BookNoteLocalDataSourceImpl(
    private val bookNoteDao: BookNoteDao,
) : BookNoteLocalDataSource {
    override fun observeAllNotesWithBook(): Flow<List<BookNoteWithBook>> =
        bookNoteDao.observeAllNotesWithBook().map { rows -> rows.map(BookNoteWithBookEntity::toDomain) }

    override suspend fun getNote(id: Long): BookNote? = bookNoteDao.findById(id)?.toDomain()

    override suspend fun getNoteWithBook(id: Long): BookNoteWithBook? = bookNoteDao.findWithBookById(id)?.toDomain()

    override fun observeNoteWithBook(id: Long): Flow<BookNoteWithBook?> =
        bookNoteDao.observeWithBookById(id).map { it?.toDomain() }

    override suspend fun saveNote(note: BookNote): Long = bookNoteDao.upsert(note.toEntity())

    override suspend fun updateNote(note: BookNote) {
        bookNoteDao.update(note.toEntity())
    }

    override suspend fun deleteNote(note: BookNote) {
        bookNoteDao.delete(note.toEntity())
    }

    override suspend fun deleteNote(id: Long) {
        bookNoteDao.deleteById(id)
    }

    override suspend fun deleteByBook(bookId: Long) {
        bookNoteDao.deleteByBook(bookId)
    }
}
