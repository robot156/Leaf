package io.github.jean.feature.settinglicense.list.model

import androidx.compose.runtime.Immutable
import io.github.jean.feature.settinglicense.list.model.section.LicenseUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class SettingLicensesState(
    val licenses: ImmutableList<LicenseUiModel> = persistentListOf(),
)
