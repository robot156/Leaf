package io.github.jean.core.datalocal.datasource.license

import android.content.Context
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.model.License
import io.github.jean.core.common.model.LicenseInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mikepenz.aboutlibraries.util.withContext as withAndroidContext

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class LicenseDataSourceImpl(
    private val context: Context,
) : LicenseDataSource {
    override suspend fun getLicenses(): List<License> =
        withContext(Dispatchers.IO) {
            Libs
                .Builder()
                .withAndroidContext(context)
                .build()
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
