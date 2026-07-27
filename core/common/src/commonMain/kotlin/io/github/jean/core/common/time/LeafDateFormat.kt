package io.github.jean.core.common.time

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

object LeafDateFormat {
    fun display(instant: Instant): String {
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${local.year}.${local.month.number.pad2()}.${local.day.pad2()}"
    }

    fun displayVerbose(instant: Instant): String {
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${local.year}년 ${local.month.number.pad2()}월 ${local.day.pad2()}일"
    }

    private fun Int.pad2() = toString().padStart(2, '0')
}
