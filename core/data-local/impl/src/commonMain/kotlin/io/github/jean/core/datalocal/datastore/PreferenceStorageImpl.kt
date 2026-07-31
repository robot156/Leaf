package io.github.jean.core.datalocal.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.common.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore 인스턴스는 플랫폼마다 파일 경로를 얻는 방법이 달라
 * androidMain / iosMain 의 binding container 가 만들어 넘긴다.
 * `DataStore<Preferences>` 를 DI 키로 노출하지 않는 이유는
 * [io.github.jean.core.datalocal.di.DatabaseBindings] 의 주석 참고.
 */
internal class PreferenceStorageImpl(
    private val dataStore: DataStore<Preferences>,
) : PreferenceStorage {
    override val theme: Flow<Theme> =
        dataStore.data.map { preferences ->
            preferences[KEY_THEME].toEnumOrDefault(Theme.System)
        }

    override val palette: Flow<LeafPalette> =
        dataStore.data.map { preferences ->
            preferences[KEY_PALETTE].toEnumOrDefault(LeafPalette.InkNavy)
        }

    override suspend fun setTheme(theme: Theme) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME] = theme.name
        }
    }

    override suspend fun setPalette(palette: LeafPalette) {
        dataStore.edit { preferences ->
            preferences[KEY_PALETTE] = palette.name
        }
    }

    // 저장된 값이 없거나 enum에 없는 이름(구버전 잔여 값 등)이면 기본값으로 안전하게 되돌린다.
    private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
        this?.let { name -> enumValues<T>().firstOrNull { it.name == name } } ?: default

    private companion object {
        val KEY_THEME = stringPreferencesKey("theme")
        val KEY_PALETTE = stringPreferencesKey("palette")
    }
}

/**
 * DataStore 파일 이름.
 *
 * Android 는 기존 `preferencesDataStore(name = "leaf_preferences")` 가 쓰던 경로
 * (`filesDir/datastore/leaf_preferences.preferences_pb`)를 그대로 유지해야 한다.
 * 이름이 바뀌면 기존 사용자의 테마·팔레트 설정이 초기화된다.
 */
internal const val PREFERENCES_NAME = "leaf_preferences"
