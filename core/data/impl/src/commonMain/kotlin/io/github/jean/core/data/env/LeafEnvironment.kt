package io.github.jean.core.data.env

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.env.BuildFlags
import io.github.jean.core.common.env.Environment

/**
 * API 주소·키·버전은 빌드 시점 상수라 [LeafBuildConfig] 로 생성되고,
 * 빌드 타입에 따라 갈리는 [BuildFlags.isDebug] 는 진입점이 주입한다.
 * 기기 정보만 플랫폼 API 가 필요해 expect/actual 로 분리한다.
 */
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class LeafEnvironment(
    private val buildFlags: BuildFlags,
) : Environment {
    override val isDebug: Boolean
        get() = buildFlags.isDebug
    override val baseApiUrl: String
        get() = LeafBuildConfig.API_URL
    override val apiKey: String
        get() = LeafBuildConfig.API_KEY
    override val appVersion: String
        get() = LeafBuildConfig.VERSION_NAME
    override val osVersion: String
        get() = platformOsVersion
    override val deviceModel: String
        get() = platformDeviceModel
}

/** Android 는 `Build.VERSION.RELEASE`, iOS 는 `UIDevice.systemVersion`. */
internal expect val platformOsVersion: String

/** Android 는 `Build.MODEL`, iOS 는 `UIDevice.model`. */
internal expect val platformDeviceModel: String
