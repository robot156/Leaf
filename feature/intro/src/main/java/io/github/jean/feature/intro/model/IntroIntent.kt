package io.github.jean.feature.intro.model

import io.github.jean.core.ui.mvi.Intent

sealed interface IntroIntent : Intent {
    data object LoadDone : IntroIntent
}
