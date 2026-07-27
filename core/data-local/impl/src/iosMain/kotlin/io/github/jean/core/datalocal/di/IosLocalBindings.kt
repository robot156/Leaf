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
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringWithContentsOfFile

@ContributesTo(AppScope::class)
@BindingContainer
object IosLocalBindings {
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
     * `aboutlibraries.json` 은 `:app-ios:exportLibraryDefinitions` 가 `iosApp/iosApp/` 에 만들고
     * Xcode 의 동기화 그룹이 앱 번들 리소스로 포함한다.
     * (Android 는 플러그인이 `res/raw` 에 넣어 주지만 iOS 에는 그 경로가 없다)
     */
    @Provides
    @SingleIn(AppScope::class)
    fun provideLicenseDataSource(): LicenseDataSource =
        LicenseDataSourceImpl(
            LibsLoader {
                Libs.Builder().withJson(readAboutLibrariesJson()).build()
            },
        )
}

/**
 * 앱 번들에 담긴 라이선스 JSON.
 *
 * 파일이 없으면 목록이 조용히 비는 대신 즉시 실패하게 둔다 —
 * 번들에서 빠졌다는 건 빌드 설정이 깨졌다는 뜻이라 조용히 넘기면 알아채기 어렵다.
 */
@OptIn(ExperimentalForeignApi::class)
private fun readAboutLibrariesJson(): String {
    val path =
        NSBundle.mainBundle.pathForResource("aboutlibraries", ofType = "json")
            ?: error("aboutlibraries.json 이 앱 번들에 없습니다. :app-ios:exportLibraryDefinitions 실행 여부를 확인하세요.")
    return checkNotNull(NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = null)) {
        "aboutlibraries.json 을 읽을 수 없습니다: $path"
    }
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
