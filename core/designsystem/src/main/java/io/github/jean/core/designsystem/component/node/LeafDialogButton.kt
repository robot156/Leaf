package io.github.jean.core.designsystem.component.node

import androidx.compose.runtime.Immutable

@Immutable
data class LeafDialogButton(
    val text: String,
    val onClick: () -> Unit,
)
