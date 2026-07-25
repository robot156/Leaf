package io.github.jean.core.datalocal.database.converter

import androidx.room.TypeConverter
import kotlin.time.Instant

/**
 * [Instant] <-> epoch milliseconds 변환. Room이 지원하지 않는 시간 타입을 저장하기 위함.
 */
class InstantConverter {
    @TypeConverter
    fun fromEpochMillis(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun toEpochMillis(instant: Instant?): Long? = instant?.toEpochMilliseconds()
}
