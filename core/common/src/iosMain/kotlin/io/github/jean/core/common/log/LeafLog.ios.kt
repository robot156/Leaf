package io.github.jean.core.common.log

import platform.Foundation.NSLog

actual fun logDebug(
    tag: String,
    message: String,
) {
    // NSLog 는 포맷 문자열을 해석하므로 message 를 직접 넘기면 %s 등이 오작동한다.
    NSLog("%s: %s", tag, message)
}
