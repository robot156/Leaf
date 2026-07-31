package io.github.jean.core.datalocal.datasource.license

import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import io.github.jean.core.common.coroutines.ioDispatcher
import io.github.jean.core.common.model.License
import io.github.jean.core.common.model.LicenseInfo
import kotlinx.coroutines.withContext

/**
 * aboutlibraries 의 생성 데이터를 읽는 방법이 플랫폼마다 다르다.
 * Android 는 `res/raw/aboutlibraries.json`, iOS 는 아직 리소스 파이프라인이 없다.
 * 도메인 매핑만 공통으로 두고 로딩은 플랫폼에 위임한다.
 */
internal fun interface LibsLoader {
    suspend fun load(): Libs
}

internal class LicenseDataSourceImpl(
    private val libsLoader: LibsLoader,
) : LicenseDataSource {
    override suspend fun getLicenses(): List<License> =
        withContext(ioDispatcher) {
            libsLoader
                .load()
                .libraries
                .map(Library::toDomain)
        }
}

private fun Library.toDomain(): License =
    License(
        uniqueId = uniqueId,
        artifactVersion = artifactVersion,
        name = name,
        // aboutlibraries 규칙과 동일하게 개발자 이름을 합치고, 없으면 조직명으로 폴백한다.
        author = developers.mapNotNull { it.name }.joinToString().ifBlank { organization?.name.orEmpty() },
        description = description,
        website = website,
        licenses =
            licenses.map { license ->
                LicenseInfo(
                    name = license.name,
                    url = license.url,
                    year = license.year,
                    spdxId = license.spdxId,
                    licenseContent = license.licenseContent,
                    hash = license.hash,
                )
            },
        tag = tag,
    )
