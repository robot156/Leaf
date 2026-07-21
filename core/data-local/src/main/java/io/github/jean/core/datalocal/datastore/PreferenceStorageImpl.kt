package io.github.jean.core.datalocal.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.model.LeafPalette
import io.github.jean.core.common.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore<Preferences>를 DI 그래프의 바인딩 키로 노출하면 상위 모듈(app)에서
// datastore 타입을 해석하지 못한다. Context만 주입받아 저장소를 이 클래스가 소유한다.
private val Context.preferenceDataStore by preferencesDataStore(name = "leaf_preferences")

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class PreferenceStorageImpl(
    context: Context,
) : PreferenceStorage {
    private val dataStore = context.preferenceDataStore
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
