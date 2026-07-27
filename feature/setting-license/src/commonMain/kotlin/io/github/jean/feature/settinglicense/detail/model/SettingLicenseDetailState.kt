package io.github.jean.feature.settinglicense.detail.model

import androidx.compose.runtime.Immutable
import io.github.jean.feature.settinglicense.detail.model.section.LicenseDetailUiModel

@Immutable
data class SettingLicenseDetailState(
    val license: LicenseDetailUiModel? = null,
)
