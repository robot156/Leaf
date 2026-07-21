package io.github.jean.feature.settinglicense.detail.model.section

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.License

@Immutable
data class LicenseDetailUiModel(
    val name: String,
    val author: String,
    val licenseName: String,
    val licenseContent: String,
)

fun License.toDetailUiModel(): LicenseDetailUiModel =
    LicenseDetailUiModel(
        name = name,
        author = author,
        licenseName = licenses.joinToString { it.name },
        licenseContent =
            licenses
                .mapNotNull { it.licenseContent?.takeIf(String::isNotBlank) }
                .joinToString("\n\n")
                .ifBlank { licenses.joinToString { it.name } },
    )
