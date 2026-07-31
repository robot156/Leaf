package io.github.jean.feature.imageviewer

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import io.github.jean.core.ui.mvi.Intent
import io.github.jean.core.ui.mvi.MVIViewModel
import io.github.jean.feature.imageviewer.model.ImageViewerIntent
import io.github.jean.feature.imageviewer.model.ImageViewerSideEffect
import io.github.jean.feature.imageviewer.model.ImageViewerState

@AssistedInject
class ImageViewerViewModel(
    @Assisted imageUrl: String,
) : MVIViewModel<ImageViewerState, ImageViewerSideEffect>(ImageViewerState(imageUrl = imageUrl)) {
    override fun onIntent(intent: Intent) {
        if (intent !is ImageViewerIntent) {
            super.onIntent(intent)
            return
        }
        intent {
            when (intent) {
                is ImageViewerIntent.CloseClick -> {
                    postSideEffect(ImageViewerSideEffect.NavigateToBack)
                }
            }
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(
            @Assisted imageUrl: String,
        ): ImageViewerViewModel
    }
}
