package io.github.jean.core.datalocal.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.jean.core.datalocal.database.entity.BookSearchCacheEntity

@Dao
interface BookSearchCacheDao {
    @Query("SELECT * FROM book_search_cache WHERE search_query = :query AND page = :page")
    suspend fun findPage(
        query: String,
        page: Int,
    ): BookSearchCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPage(entity: BookSearchCacheEntity)

    @Query("DELETE FROM book_search_cache")
    suspend fun deleteAll()
}
