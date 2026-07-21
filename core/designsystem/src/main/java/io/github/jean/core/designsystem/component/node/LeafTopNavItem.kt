package io.github.jean.core.designsystem.component.node

import androidx.annotation.DrawableRes

sealed interface LeafTopNavItem {
    data class Icon(
        @DrawableRes val iconRes: Int,
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
