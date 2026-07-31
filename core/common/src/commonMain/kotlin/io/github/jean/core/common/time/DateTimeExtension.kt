package io.github.jean.core.common.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

fun now(): Instant = Clock.System.now()

fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime =
    Clock.System.now().toLocalDateTime(timeZone)

fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()) = LocalDateTime.now(timeZone = timeZone).date

fun LocalDate.toInstant(timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant = atStartOfDayIn(timeZone)

fun Instant.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()) = this.toLocalDateTime(timeZone).date
