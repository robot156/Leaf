package io.github.jean.core.datalocal.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.github.jean.core.datalocal.database.LeafDatabase
import io.github.jean.core.datalocal.database.dao.BookDao
import io.github.jean.core.datalocal.database.dao.BookNoteDao
import io.github.jean.core.datalocal.database.dao.BookSearchCacheDao

/**
 * 플랫폼 무관한 바인딩만 여기 둔다.
 *
 * [LeafDatabase] 인스턴스 · DataStore · 이미지 캐시 · 라이선스는 플랫폼마다 생성 방법이 달라
 * androidMain / iosMain 의 binding container 가 제공한다.
 *
 * 그래프에 노출하는 키를 이 모듈(또는 data-local:api)의 타입으로만 제한한다.
 * `DataStore<Preferences>` 나 `RoomDatabase.Builder` 처럼 서드파티 타입을 키로 쓰면
 * 그래프를 생성하는 app 모듈이 그 타입을 해석하지 못한다. (기존 주석에 적혀 있던 제약)
 */
@ContributesTo(AppScope::class)
@BindingContainer
object DatabaseBindings {
    @Provides
    fun provideBookDao(database: LeafDatabase): BookDao = database.bookDao()

    @Provides
    fun provideBookNoteDao(database: LeafDatabase): BookNoteDao = database.bookNoteDao()

    @Provides
    fun provideBookSearchCacheDao(database: LeafDatabase): BookSearchCacheDao = database.bookSearchCacheDao()
}
