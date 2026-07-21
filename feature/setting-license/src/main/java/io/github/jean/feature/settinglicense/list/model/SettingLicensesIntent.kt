package io.github.jean.feature.settinglicense.list.model

import io.github.jean.core.ui.mvi.Intent

sealed interface SettingLicensesIntent : Intent {
    data object BackClick : SettingLicensesIntent

    data class CardClick(
        val uniqueId: String,
    ) : SettingLicensesIntent
}
