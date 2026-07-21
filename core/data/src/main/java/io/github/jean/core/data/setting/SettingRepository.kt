package io.github.jean.core.data.setting

import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.common.model.License
import io.github.jean.core.common.model.Theme
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    val theme: Flow<Theme>

    val palette: Flow<LeafPalette>

    suspend fun getLicenses(): List<License>

    suspend fun getLicense(uniqueId: String): License?

    suspend fun setTheme(theme: Theme)

    suspend fun setPalette(palette: LeafPalette)

    suspend fun clearCache()
}
