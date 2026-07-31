package io.github.jean.core.designsystem.component.node

import org.jetbrains.compose.resources.DrawableResource

sealed interface LeafTopNavItem {
    data class Icon(
        val iconRes: DrawableResource,
        val onClick: () -> Unit,
        val enabled: Boolean = true,
        val contentDescription: String? = null,
    ) : LeafTopNavItem

    data class TextButton(
        val text: String,
        val onClick: () -> Unit,
        val enabled: Boolean = true,
    ) : LeafTopNavItem
}
