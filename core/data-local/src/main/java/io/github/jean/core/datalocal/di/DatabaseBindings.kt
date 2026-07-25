package io.github.jean.core.datalocal.di

import android.content.Context
import androidx.room.Room
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.datalocal.database.LeafDatabase
import io.github.jean.core.datalocal.database.dao.BookDao
import io.github.jean.core.datalocal.database.dao.BookNoteDao
import io.github.jean.core.datalocal.database.dao.BookSearchCacheDao

@ContributesTo(AppScope::class)
@BindingContainer
object DatabaseBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideLeafDatabase(context: Context): LeafDatabase =
        Room
            .databaseBuilder(
                context = context,
                klass = LeafDatabase::class.java,
                name = LeafDatabase.NAME,
            ).build()

    @Provides
    fun provideBookDao(database: LeafDatabase): BookDao = database.bookDao()

    @Provides
    fun provideBookNoteDao(database: LeafDatabase): BookNoteDao = database.bookNoteDao()

    @Provides
    fun provideBookSearchCacheDao(database: LeafDatabase): BookSearchCacheDao = database.bookSearchCacheDao()
}
