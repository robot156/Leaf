package io.github.jean.core.ui.navigation

import androidx.compose.runtime.Composable

/**
 * 뒤로가기 가로채기.
 *
 * Android 는 activity-compose 의 `BackHandler`(예측형 뒤로가기까지 검증됨)를 그대로 쓰고,
 * iOS 는 navigationevent 의 `NavigationBackHandler` 로 연결한다.
 * 플랫폼 API 가 달라 여기서 한 번만 감싸 둔다.
 *
 * @param enabled false 면 가로채지 않고 기본 뒤로가기가 동작한다.
 */
@Composable
expect fun LeafBackHandler(
    enabled: Boolean,
    onBack: () -> Unit,
)
