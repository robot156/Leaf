package io.github.jean.core.datalocal.datastore

import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.common.model.Theme
import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {
    val theme: Flow<Theme>

    val palette: Flow<LeafPalette>

    suspend fun setTheme(theme: Theme)

    suspend fun setPalette(palette: LeafPalette)
}
