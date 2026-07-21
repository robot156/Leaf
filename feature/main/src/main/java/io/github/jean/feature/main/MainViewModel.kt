package io.github.jean.feature.main

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.jean.core.data.setting.SettingRepository
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.main.model.MainState
import kotlinx.coroutines.flow.combine
import org.orbitmvi.orbit.syntax.Syntax

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
class MainViewModel(
    private val settingRepository: SettingRepository,
) : MVIViewModel<MainState, Unit>(MainState()) {

    override suspend fun Syntax<MainState, Unit>.onContainerCreate() {
        repeatOnSubscription {
            combine(
                settingRepository.theme,
                settingRepository.palette,
            ) { theme, palette -> theme to palette }
                .collect { (theme, palette) ->
                    reduce {
                        state.copy(
                            theme = theme,
                            palette = palette,
                        )
                    }
                }
        }
    }
}
