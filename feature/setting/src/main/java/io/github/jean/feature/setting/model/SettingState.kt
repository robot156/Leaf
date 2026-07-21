package io.github.jean.feature.setting.model

import androidx.compose.runtime.Immutable
import io.github.jean.core.common.model.Theme

@Immutable
data class SettingState(
    val theme: Theme = Theme.System,
    val appVersion: String = "",
    val isShowClearCacheDialog: Boolean = false,
)
