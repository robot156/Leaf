package io.github.jean.core.data.setting

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.common.model.License
import io.github.jean.core.common.model.Theme
import io.github.jean.core.datalocal.datasource.book.BookLocalDataSource
import io.github.jean.core.datalocal.datasource.image.ImageCacheDataSource
import io.github.jean.core.datalocal.datasource.license.LicenseDataSource
import io.github.jean.core.datalocal.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class SettingRepositoryImpl(
    private val preferenceStorage: PreferenceStorage,
    private val imageCacheDataSource: ImageCacheDataSource,
    private val bookLocalDataSource: BookLocalDataSource,
    private val licenseDataSource: LicenseDataSource,
) : SettingRepository {
    override val theme: Flow<Theme> = preferenceStorage.theme

    override val palette: Flow<LeafPalette> = preferenceStorage.palette

    override suspend fun getLicenses(): List<License> = licenseDataSource.getLicenses()

    override suspend fun getLicense(uniqueId: String): License? =
        licenseDataSource.getLicenses().firstOrNull { it.uniqueId == uniqueId }

    override suspend fun setTheme(theme: Theme) {
        preferenceStorage.setTheme(theme)
    }

    override suspend fun setPalette(palette: LeafPalette) {
        preferenceStorage.setPalette(palette)
    }

    override suspend fun clearCache() {
        imageCacheDataSource.clear()
        bookLocalDataSource.clearSearchCache()
    }
}
