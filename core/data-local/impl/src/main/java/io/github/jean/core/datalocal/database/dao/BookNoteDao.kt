package io.github.jean.core.datalocal.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.github.jean.core.datalocal.database.entity.BookNoteEntity
import io.github.jean.core.datalocal.database.entity.BookNoteWithBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookNoteDao {
    @Transaction
    @Query("SELECT * FROM book_note ORDER BY created_at DESC")
    fun observeAllNotesWithBook(): Flow<List<BookNoteWithBookEntity>>

    @Query("SELECT * FROM book_note WHERE id = :id")
    suspend fun findById(id: Long): BookNoteEntity?

    @Transaction
    @Query("SELECT * FROM book_note WHERE id = :id")
    suspend fun findWithBookById(id: Long): BookNoteWithBookEntity?

    @Transaction
    @Query("SELECT * FROM book_note WHERE id = :id")
    fun observeWithBookById(id: Long): Flow<BookNoteWithBookEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: BookNoteEntity): Long

    @Update
    suspend fun update(note: BookNoteEntity)

    @Delete
    suspend fun delete(note: BookNoteEntity)

    @Query("DELETE FROM book_note WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM book_note WHERE book_id = :bookId")
    suspend fun deleteByBook(bookId: Long)
}
