package io.github.jean.core.datalocal.datasource.license

import io.github.jean.core.common.model.License

interface LicenseDataSource {
    suspend fun getLicenses(): List<License>
}
