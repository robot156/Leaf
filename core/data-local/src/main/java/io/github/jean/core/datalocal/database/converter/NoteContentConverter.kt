package io.github.jean.core.datalocal.database.converter

import androidx.room.TypeConverter
import io.github.jean.core.datalocal.database.entity.NoteContentEntity
import kotlinx.serialization.json.Json

/**
 * [NoteContentEntity] 블록 리스트를 JSON 문자열 컬럼으로 저장한다.
 * sealed 다형성 직렬화는 각 하위 타입의 @SerialName("quote"/"record")을 판별자로 사용한다.
 */
class NoteContentConverter {
    @TypeConverter
    fun fromJson(value: String?): List<NoteContentEntity> =
        value?.let {
            json.decodeFromString<List<NoteContentEntity>>(it)
        } ?: emptyList()

    @TypeConverter
    fun toJson(content: List<NoteContentEntity>): String = json.encodeToString(content)

    private companion object {
        val json = Json { ignoreUnknownKeys = true }
    }
}
