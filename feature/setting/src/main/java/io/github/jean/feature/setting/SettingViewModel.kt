package io.github.jean.feature.setting

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.jean.core.common.env.Environment
import io.github.jean.core.data.setting.SettingRepository
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.setting.model.SettingIntent
import io.github.jean.feature.setting.model.SettingSideEffect
import io.github.jean.feature.setting.model.SettingState
import org.orbitmvi.orbit.syntax.Syntax

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
class SettingViewModel(
    private val settingRepository: SettingRepository,
    private val environment: Environment,
) : MVIViewModel<SettingState, SettingSideEffect>(SettingState()) {
    override suspend fun Syntax<SettingState, SettingSideEffect>.onContainerCreate() {
        repeatOnSubscription {
            settingRepository.theme.collect { theme ->
                reduce {
                    state.copy(
                        theme = theme,
                        appVersion = environment.appVersion,
                    )
                }
            }
        }
    }

    override fun onIntent(intent: Intent) {
        if (intent !is SettingIntent) {
            super.onIntent(intent)
            return
        }

        intent {
            when (intent) {
                is SettingIntent.BackClick -> {
                    postSideEffect(SettingSideEffect.NavigateToBack)
                }

                is SettingIntent.ThemeClick -> {
                    postSideEffect(SettingSideEffect.NavigateToThemeSetting)
                }

                is SettingIntent.ContactClick -> {
                    postSideEffect(
                        SettingSideEffect.NavigateToEmail(
                            appVersion = environment.appVersion,
                            osVersion = environment.osVersion,
                            deviceModel = environment.deviceModel,
                        ),
                    )
                }

                is SettingIntent.ClearCacheClick -> {
                    reduce { state.copy(isShowClearCacheDialog = true) }
                }

                is SettingIntent.ClearCacheDialogDismiss -> {
                    reduce { state.copy(isShowClearCacheDialog = false) }
                }

                is SettingIntent.ClearCacheConfirm -> {
                    settingRepository.clearCache()
                    reduce { state.copy(isShowClearCacheDialog = false) }
                }

                is SettingIntent.LicensesClick -> {
                    postSideEffect(SettingSideEffect.NavigateToLicenses)
                }
            }
        }
    }
}
