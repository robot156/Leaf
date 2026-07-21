package io.github.jean.feature.settingtheme

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.jean.core.data.setting.SettingRepository
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.settingtheme.model.SettingThemeIntent
import io.github.jean.feature.settingtheme.model.SettingThemeSideEffect
import io.github.jean.feature.settingtheme.model.SettingThemeState
import kotlinx.coroutines.flow.combine
import org.orbitmvi.orbit.syntax.Syntax

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
class SettingThemeViewModel(
    private val settingRepository: SettingRepository,
) : MVIViewModel<SettingThemeState, SettingThemeSideEffect>(SettingThemeState()) {

    override suspend fun Syntax<SettingThemeState, SettingThemeSideEffect>.onContainerCreate() {
        repeatOnSubscription {
            combine(
                settingRepository.theme,
                settingRepository.palette,
            ) { theme, palette -> theme to palette }
                .collect { (theme, palette) ->
                    reduce {
                        state.copy(
                            currentTheme = theme,
                            currentPalette = palette,
                        )
                    }
                }
        }
    }

    override fun onIntent(intent: Intent) {
        if (intent !is SettingThemeIntent) {
            super.onIntent(intent)
            return
        }

        intent {
            when (intent) {
                is SettingThemeIntent.BackClick -> {
                    postSideEffect(SettingThemeSideEffect.NavigateToBack)
                }

                is SettingThemeIntent.ThemeSelect -> {
                    settingRepository.setTheme(intent.theme)
                }

                is SettingThemeIntent.PaletteSelect -> {
                    settingRepository.setPalette(intent.palette)
                }
            }
        }
    }
}
