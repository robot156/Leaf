package io.github.jean.core.common.log

/**
 * 플랫폼 디버그 로그.
 *
 * Android 는 `Log.d`, iOS 는 `NSLog` 로 내려간다.
 * 릴리스 빌드에서 호출을 막는 책임은 호출부(예: `Environment.isDebug`)에 있다.
 */
expect fun logDebug(
    tag: String,
    message: String,
)
