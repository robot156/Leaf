package io.github.jean.core.ui.animation

import androidx.compose.runtime.Composable

typealias MaskAnimActive = (MaskAnimModel, Float, Float) -> Unit

/**
 * 테마 전환용 원형 리빌 애니메이션.
 *
 * 전환 직전 화면을 스냅샷으로 떠서 새 화면 위에 덮고, 클릭 지점에서 원형으로 구멍을 넓혀
 * 아래의 새 테마를 드러낸다. 스냅샷을 **동기적으로** 떠야 하는 게 핵심이다 —
 * 캡처가 늦으면 이미 바뀐 새 화면을 찍어 효과가 무의미해진다.
 *
 * Android 는 `View.draw(Canvas)` 로 동기 캡처가 되지만,
 * CMP 의 `GraphicsLayer.toImageBitmap()` 은 `suspend` 라 같은 타이밍을 보장할 수 없다.
 * 그래서 검증된 Android 구현을 그대로 두고 플랫폼별로 분리했다.
 *
 * TODO(iOS): 현재 iOS 는 애니메이션 없이 즉시 전환한다.
 *   UIKit 의 `snapshotView(afterScreenUpdates: false)` 나 CMP 의 그래픽스 레이어를
 *   프레임 단위로 미리 기록해 두는 방식으로 대체할 수 있다.
 */
@Composable
expect fun MaskBox(
    animationDurationMillis: Long = 650L,
    content: @Composable (MaskAnimActive) -> Unit,
)
