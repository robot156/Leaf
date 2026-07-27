package io.github.jean.feature.intro.model

sealed interface IntroSideEffect {
    data object NavigateToHome : IntroSideEffect
}
