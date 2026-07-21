package io.github.jean.core.datalocal.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

/**
 * 검색 페이지 캐시의 itemId 목록을 JSON 문자열 컬럼으로 저장한다. 순서를 보존한다.
 */
internal class LongListConverter {
    @TypeConverter
    fun fromJson(value: String?): List<Long> = value?.let { json.decodeFromString<List<Long>>(it) } ?: emptyList()

    @TypeConverter
    fun toJson(value: List<Long>): String = json.encodeToString(value)

    private companion object {
        val json = Json
    }
}
