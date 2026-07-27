package io.github.jean.feature.main.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.UIKit.UIApplication

@Composable
actual fun rememberPlatformActions(): PlatformActions = remember { IosPlatformActions() }

private class IosPlatformActions : PlatformActions {
    override fun openWeb(link: String) {
        if (link.isBlank()) return
        NSURL.URLWithString(link)?.let(::open)
    }

    override fun sendContactMail(
        appVersion: String,
        osVersion: String,
        device: String,
    ) {
        // mailto 스킴은 본문·제목을 퍼센트 인코딩해야 한다. NSURLComponents 가 처리해 준다.
        val components = NSURLComponents()
        components.setScheme("mailto")
        components.setPath(CONTACT_EMAIL)
        components.setQueryItems(
            listOf(
                NSURLQueryItem(name = "subject", value = CONTACT_SUBJECT),
                NSURLQueryItem(
                    name = "body",
                    value =
                        contactMailBody(
                            appVersion = appVersion,
                            osVersion = osVersion,
                            device = device,
                            osName = "iOS",
                        ),
                ),
            ),
        )
        components.URL?.let(::open)
    }

    /**
     * TODO(iOS): `UIActivityViewController` 로 공유해야 하지만 표시할 `UIViewController` 참조가 필요하다.
     *   8단계에서 진입점(`ComposeUIViewController`)을 만들 때 루트 VC 를 넘겨받아 구현한다.
     *   그때까지 노트 이미지 공유는 iOS 에서 동작하지 않는다.
     */
    override fun shareImage(image: ImageBitmap) = Unit

    private fun open(url: NSURL) {
        UIApplication.sharedApplication.openURL(url)
    }
}
