package io.github.jean.feature.settinglicense.detail

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import io.github.jean.core.data.setting.SettingRepository
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.settinglicense.detail.model.SettingLicenseDetailIntent
import io.github.jean.feature.settinglicense.detail.model.SettingLicenseDetailSideEffect
import io.github.jean.feature.settinglicense.detail.model.SettingLicenseDetailState
import io.github.jean.feature.settinglicense.detail.model.section.toDetailUiModel
import org.orbitmvi.orbit.syntax.Syntax

@AssistedInject
class SettingLicenseDetailViewModel(
    @Assisted private val uniqueId: String,
    private val settingRepository: SettingRepository,
) : MVIViewModel<SettingLicenseDetailState, SettingLicenseDetailSideEffect>(SettingLicenseDetailState()) {
    override suspend fun Syntax<SettingLicenseDetailState, SettingLicenseDetailSideEffect>.onContainerCreate() {
        val license = settingRepository.getLicense(uniqueId)
        if (license == null) {
            postSideEffect(SettingLicenseDetailSideEffect.NavigateToBack)
            return
        }

        reduce { state.copy(license = license.toDetailUiModel()) }
    }

    override fun onIntent(intent: Intent) {
        if (intent !is SettingLicenseDetailIntent) {
            super.onIntent(intent)
            return
        }

        intent {
            when (intent) {
                is SettingLicenseDetailIntent.BackClick -> {
                    postSideEffect(SettingLicenseDetailSideEffect.NavigateToBack)
                }
            }
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(
            @Assisted uniqueId: String,
        ): SettingLicenseDetailViewModel
    }
}
