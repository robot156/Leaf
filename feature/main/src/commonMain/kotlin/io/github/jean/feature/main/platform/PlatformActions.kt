package io.github.jean.feature.main.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

/**
 * 앱 밖으로 나가는 동작들. 플랫폼 API 가 전부 달라 여기로 모았다.
 *
 * [rememberPlatformActions] 로 얻는다. Android 구현은 `LocalContext` 가 필요해서
 * 컴포지션 안에서만 만들 수 있다.
 */
interface PlatformActions {
    /** 외부 브라우저로 링크 열기. 빈 문자열이면 아무 것도 하지 않는다. */
    fun openWeb(link: String)

    /** 문의 메일 작성 화면 열기. 본문에 앱/기기 정보를 채운다. */
    fun sendContactMail(
        appVersion: String,
        osVersion: String,
        device: String,
    )

    /** 노트 이미지를 다른 앱으로 공유. */
    fun shareImage(image: ImageBitmap)
}

@Composable
expect fun rememberPlatformActions(): PlatformActions

internal const val CONTACT_EMAIL = "gim162913@gmail.com"
internal const val CONTACT_SUBJECT = "[Leaf] 문의"

/** 메일 본문. 플랫폼별 구현이 같은 문구를 쓰도록 공용으로 둔다. */
internal fun contactMailBody(
    appVersion: String,
    osVersion: String,
    device: String,
    osName: String,
): String =
    """


    ─────────────
    앱 버전: $appVersion
    기기: $device
    OS: $osName $osVersion
    """.trimIndent()
