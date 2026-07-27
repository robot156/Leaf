package io.github.jean.core.data.env

import platform.UIKit.UIDevice

internal actual val platformOsVersion: String
    get() = UIDevice.currentDevice.systemVersion

/**
 * Android 의 `Build.MODEL` 은 "SM-G991N" 처럼 구체적인 기기명을 주지만
 * iOS 의 `UIDevice.model` 은 "iPhone" 수준이다.
 * 정확한 기기 식별자가 필요해지면 `sysctlbyname("hw.machine")` 으로 바꿔야 한다.
 */
internal actual val platformDeviceModel: String
    get() = UIDevice.currentDevice.model
