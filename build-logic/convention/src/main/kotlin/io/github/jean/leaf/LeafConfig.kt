package io.github.jean.leaf

import org.gradle.api.JavaVersion

object LeafConfig {
    const val APPLICATION_ID = "io.github.jean.leaf"

    const val MIN_SDK = 29
    const val TARGET_SDK = 37
    const val COMPILE_SDK = 37
    val javaCompileTarget = JavaVersion.VERSION_11

    // Calendar-based versioning: <year>.<month>.<release>
    // e.g. 2026.07.00 — the release counter increments per deployment within a month.
    private const val VERSION_YEAR = 2026
    private const val VERSION_MONTH = 7
    private const val VERSION_RELEASE = 0

    val VERSION_NAME = "%d.%02d.%02d".format(VERSION_YEAR, VERSION_MONTH, VERSION_RELEASE)
    const val VERSION_CODE = VERSION_YEAR.times(10000) + VERSION_MONTH.times(100) + VERSION_RELEASE
}
