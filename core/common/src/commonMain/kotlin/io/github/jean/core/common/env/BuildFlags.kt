package io.github.jean.core.common.env

/**
 * 빌드 타입에 따라 값이 달라지는 플래그.
 *
 * KMP android 라이브러리 타깃은 `buildConfig` 도 빌드 타입도 지원하지 않는다(단일 variant).
 * 그래서 라이브러리 모듈에서는 `BuildConfig.DEBUG` 를 쓸 수 없다.
 * 빌드 타입을 아는 진입점(app / iosApp)이 그래프에 넣어준다.
 */
data class BuildFlags(
    val isDebug: Boolean,
)
