package io.github.jean.core.datalocal.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mikepenz.aboutlibraries.Libs
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.datalocal.database.LeafDatabase
import io.github.jean.core.datalocal.datasource.image.ImageCacheDataSource
import io.github.jean.core.datalocal.datasource.image.ImageCacheDataSourceImpl
import io.github.jean.core.datalocal.datasource.license.LibsLoader
import io.github.jean.core.datalocal.datasource.license.LicenseDataSource
import io.github.jean.core.datalocal.datasource.license.LicenseDataSourceImpl
import io.github.jean.core.datalocal.datastore.PREFERENCES_NAME
import io.github.jean.core.datalocal.datastore.PreferenceStorage
import io.github.jean.core.datalocal.datastore.PreferenceStorageImpl
import com.mikepenz.aboutlibraries.util.withContext as withAndroidContext

/**
 * 플랫폼마다 생성 방법이 다른 것들을 Android 쪽에서 조립한다.
 * 노출하는 키는 모두 이 모듈 또는 data-local:api 의 타입이다.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object AndroidLocalBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideLeafDatabase(context: Context): LeafDatabase =
        Room
            .databaseBuilder<LeafDatabase>(
                context = context,
                // 상대 이름을 그대로 유지해야 기존 사용자의 DB 파일을 계속 쓴다.
                name = LeafDatabase.NAME,
            ).setDriver(BundledSQLiteDriver())
            .build()

    @Provides
    @SingleIn(AppScope::class)
    fun providePreferenceStorage(context: Context): PreferenceStorage =
        PreferenceStorageImpl(
            // preferencesDataStoreFile 은 기존 preferencesDataStore 위임과 동일한 경로를 준다.
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile(PREFERENCES_NAME) },
            ),
        )

    // Android 에서 coil3.PlatformContext 는 android.content.Context 의 typealias 라 그대로 넘긴다.
    @Provides
    @SingleIn(AppScope::class)
    fun provideImageCacheDataSource(context: Context): ImageCacheDataSource = ImageCacheDataSourceImpl(context)

    @Provides
    @SingleIn(AppScope::class)
    fun provideLicenseDataSource(context: Context): LicenseDataSource =
        LicenseDataSourceImpl(
            LibsLoader {
                Libs
                    .Builder()
                    .withAndroidContext(context)
                    .build()
            },
        )
}
