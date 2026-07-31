package io.github.jean.core.designsystem.theme

import androidx.compose.ui.text.PlatformTextStyle

/**
 * Android 는 폰트 메트릭에서 오는 상하 여백이 한글에서 특히 두드러진다.
 * 끄지 않으면 줄 높이 중앙 정렬이 어긋난다.
 */
internal actual fun koreanPlatformTextStyle(): PlatformTextStyle? = PlatformTextStyle(includeFontPadding = false)
