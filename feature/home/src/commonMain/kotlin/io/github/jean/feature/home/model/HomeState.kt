package io.github.jean.feature.home.model

import androidx.compose.runtime.Immutable
import io.github.jean.feature.home.model.section.HomeBookUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class HomeState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val bookNotes: ImmutableList<HomeBookUiModel> = persistentListOf(),
)
