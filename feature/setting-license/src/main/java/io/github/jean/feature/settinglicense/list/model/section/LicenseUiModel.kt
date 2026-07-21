package io.github.jean.feature.settinglicense.list.model.section

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.License

@Immutable
data class LicenseUiModel(
    val uniqueId: String,
    val name: String,
    val version: String?,
    val author: String,
    val licenseName: String,
)

fun License.toUiModel(): LicenseUiModel =
    LicenseUiModel(
        uniqueId = uniqueId,
        name = name,
        version = artifactVersion,
        author = author,
        licenseName = licenses.joinToString { it.name },
    )
