package io.github.jean.core.designsystem.theme

import androidx.compose.ui.text.PlatformTextStyle

/** iOS 에는 Android 의 `includeFontPadding` 에 해당하는 개념이 없다. */
internal actual fun koreanPlatformTextStyle(): PlatformTextStyle? = null
