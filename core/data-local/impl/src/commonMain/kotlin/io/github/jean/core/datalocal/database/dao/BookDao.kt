package io.github.jean.core.datalocal.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import io.github.jean.core.datalocal.database.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book ORDER BY created_at DESC")
    fun observeAll(): Flow<List<BookEntity>>

    @Query("SELECT * FROM book ORDER BY created_at DESC")
    suspend fun getAll(): List<BookEntity>

    @Query("SELECT * FROM book WHERE item_id IN (:itemIds)")
    suspend fun findByItemIds(itemIds: List<Long>): List<BookEntity>

    @Query("SELECT * FROM book WHERE item_id = :itemId")
    fun observeByItemId(itemId: Long): Flow<BookEntity?>

    @Query("SELECT * FROM book WHERE item_id = :itemId")
    suspend fun findByItemId(itemId: Long): BookEntity?

    // REPLACE는 충돌 행을 DELETE 후 재삽입해 book_note의 FK CASCADE를 유발할 수 있어
    // 비파괴적으로 UPDATE하는 @Upsert를 쓴다.
    @Upsert
    suspend fun upsert(book: BookEntity): Long

    @Upsert
    suspend fun upsertAll(books: List<BookEntity>): List<Long>

    @Update
    suspend fun update(book: BookEntity)

    @Delete
    suspend fun delete(book: BookEntity)

    @Query("DELETE FROM book WHERE item_id = :itemId")
    suspend fun deleteByItemId(itemId: Long)
}
