package io.github.jean.feature.intro

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.intro.model.IntroIntent
import io.github.jean.feature.intro.model.IntroSideEffect
import io.github.jean.feature.intro.model.IntroState

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
class IntroViewModel : MVIViewModel<IntroState, IntroSideEffect>(IntroState) {
    override fun onIntent(intent: Intent) {
        if (intent !is IntroIntent) {
            super.onIntent(intent)
            return
        }

        intent {
            when (intent) {
                is IntroIntent.LoadDone -> {
                    postSideEffect(IntroSideEffect.NavigateToHome)
                }
            }
        }
    }
}
