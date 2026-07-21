package io.github.jean.core.datalocal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.jean.core.datalocal.database.converter.InstantConverter
import io.github.jean.core.datalocal.database.converter.LongListConverter
import io.github.jean.core.datalocal.database.converter.NoteContentConverter
import io.github.jean.core.datalocal.database.dao.BookDao
import io.github.jean.core.datalocal.database.dao.BookNoteDao
import io.github.jean.core.datalocal.database.dao.BookSearchCacheDao
import io.github.jean.core.datalocal.database.entity.BookEntity
import io.github.jean.core.datalocal.database.entity.BookNoteEntity
import io.github.jean.core.datalocal.database.entity.BookSearchCacheEntity

@Database(
    entities = [
        BookEntity::class,
        BookNoteEntity::class,
        BookSearchCacheEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    NoteContentConverter::class,
    LongListConverter::class,
)
abstract class LeafDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun bookNoteDao(): BookNoteDao

    abstract fun bookSearchCacheDao(): BookSearchCacheDao

    companion object {
        const val NAME = "leaf.db"
    }
}
