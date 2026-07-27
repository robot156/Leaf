package io.github.jean.core.datalocal.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import coil3.PlatformContext
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
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@ContributesTo(AppScope::class)
@BindingContainer
object IosLocalBindings {
    private const val EMPTY_LIBS_JSON = """{"libraries":[],"licenses":{}}"""

    @Provides
    @SingleIn(AppScope::class)
    fun provideLeafDatabase(): LeafDatabase =
        Room
            .databaseBuilder<LeafDatabase>(name = "${documentDirectory()}/${LeafDatabase.NAME}")
            .setDriver(BundledSQLiteDriver())
            .build()

    @Provides
    @SingleIn(AppScope::class)
    fun providePreferenceStorage(): PreferenceStorage =
        PreferenceStorageImpl(
            PreferenceDataStoreFactory.createWithPath(
                produceFile = { "${documentDirectory()}/$PREFERENCES_NAME.preferences_pb".toPath() },
            ),
        )

    @Provides
    @SingleIn(AppScope::class)
    fun provideImageCacheDataSource(): ImageCacheDataSource = ImageCacheDataSourceImpl(PlatformContext.INSTANCE)

    /**
     * iOS 는 아직 aboutlibraries JSON 을 담을 리소스 경로가 없다.
     * 5단계에서 compose-resources(`composeResources/files/aboutlibraries.json`)가 붙으면
     * 그 파일을 읽도록 바꾼다. 그때까지 설정 화면의 라이선스 목록은 비어 있다.
     */
    @Provides
    @SingleIn(AppScope::class)
    fun provideLicenseDataSource(): LicenseDataSource =
        LicenseDataSourceImpl(
            LibsLoader {
                Libs.Builder().withJson(EMPTY_LIBS_JSON).build()
            },
        )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val url: NSURL? =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    return requireNotNull(url?.path) { "NSDocumentDirectory 경로를 얻을 수 없다" }
}
