package io.github.jean.core.common.model

data class License(
    val uniqueId: String,
    val artifactVersion: String?,
    val name: String,
    val author: String,
    val description: String?,
    val website: String?,
    val licenses: List<LicenseInfo>,
    val tag: String?,
)

data class LicenseInfo(
    val name: String,
    val url: String?,
    val year: String?,
    val spdxId: String?,
    val licenseContent: String?,
    val hash: String,
)
