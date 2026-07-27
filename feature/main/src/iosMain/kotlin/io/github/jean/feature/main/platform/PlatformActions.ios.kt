package io.github.jean.feature.main.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import org.jetbrains.skia.Image as SkiaImage

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

    override fun shareImage(image: ImageBitmap) {
        val uiImage = image.toUIImage() ?: return
        val presenter = topViewController() ?: return

        // iPad 를 지원하게 되면 popover 앵커(sourceView)를 지정해야 한다.
        // 지금은 iPhone 전용(TARGETED_DEVICE_FAMILY = 1)이라 시트로 올라온다.
        val activityViewController =
            UIActivityViewController(
                activityItems = listOf(uiImage),
                applicationActivities = null,
            )
        presenter.presentViewController(activityViewController, animated = true, completion = null)
    }

    /**
     * `openURL(_:)`(인자 하나짜리)은 iOS 10 부터 deprecated 이고 최신 iOS 에서는 아무 일도 하지 않는다.
     * 반드시 `openURL:options:completionHandler:` 를 써야 한다.
     */
    private fun open(url: NSURL) {
        UIApplication.sharedApplication.openURL(
            url = url,
            options = emptyMap<Any?, Any>(),
            completionHandler = null,
        )
    }
}

/**
 * 공유 시트를 띄울 뷰 컨트롤러.
 *
 * 이미 무언가 present 된 상태라면 그 위에 올려야 한다.
 * (그러지 않으면 "presenting a view controller which is already presenting" 로 무시된다)
 */
private fun topViewController(): UIViewController? {
    val keyWindow =
        UIApplication.sharedApplication.windows
            .filterIsInstance<UIWindow>()
            .firstOrNull { it.isKeyWindow() }
            ?: UIApplication.sharedApplication.windows.firstOrNull() as? UIWindow

    var controller = keyWindow?.rootViewController
    while (controller?.presentedViewController != null) {
        controller = controller.presentedViewController
    }
    return controller
}

@OptIn(ExperimentalForeignApi::class)
private fun ImageBitmap.toUIImage(): UIImage? {
    val pngBytes =
        SkiaImage
            .makeFromBitmap(asSkiaBitmap())
            .encodeToData()
            ?.bytes
            ?: return null

    if (pngBytes.isEmpty()) return null

    val data =
        pngBytes.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = pngBytes.size.toULong())
        }
    return UIImage(data = data)
}
