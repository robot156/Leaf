package io.github.jean.core.ui.animation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * iOS 는 아직 원형 리빌 없이 즉시 전환한다. 자세한 배경은 commonMain 의 expect 선언 참고.
 * [MaskAnimActive] 는 호출되어도 아무 일도 하지 않으므로 테마 변경 자체는 정상 동작한다.
 */
@Composable
actual fun MaskBox(
    animationDurationMillis: Long,
    content: @Composable (MaskAnimActive) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content { _, _, _ -> }
    }
}
