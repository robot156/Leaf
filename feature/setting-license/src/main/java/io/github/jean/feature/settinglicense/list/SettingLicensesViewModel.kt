package io.github.jean.feature.settinglicense.list

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.jean.core.data.setting.SettingRepository
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.settinglicense.list.model.SettingLicensesIntent
import io.github.jean.feature.settinglicense.list.model.SettingLicensesSideEffect
import io.github.jean.feature.settinglicense.list.model.SettingLicensesState
import io.github.jean.feature.settinglicense.list.model.section.toUiModel
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.syntax.Syntax

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
class SettingLicensesViewModel(
    private val settingRepository: SettingRepository,
) : MVIViewModel<SettingLicensesState, SettingLicensesSideEffect>(SettingLicensesState()) {
    override suspend fun Syntax<SettingLicensesState, SettingLicensesSideEffect>.onContainerCreate() {
        runCatching { settingRepository.getLicenses() }
            .onSuccess { licenses ->
                reduce {
                    state.copy(
                        licenses =
                            licenses
                                .sortedByDescending { it.tag == "font" }
                                .map { it.toUiModel() }
                                .toPersistentList(),
                    )
                }
            }
    }

    override fun onIntent(intent: Intent) {
        if (intent !is SettingLicensesIntent) {
            super.onIntent(intent)
            return
        }

        intent {
            when (intent) {
                is SettingLicensesIntent.BackClick -> {
                    postSideEffect(SettingLicensesSideEffect.NavigateToBack)
                }

                is SettingLicensesIntent.CardClick -> {
                    postSideEffect(SettingLicensesSideEffect.NavigateToLicenseDetail(intent.uniqueId))
                }
            }
        }
    }
}
