package io.github.jean.leaf.ios

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import io.github.jean.core.common.env.BuildFlags

/**
 * iOS DI 그래프.
 *
 * Android 의 `AndroidAppGraph` 와 달리 `MetroAppComponentProviders`(Activity 주입)를
 * 상속하지 않는다 — metrox-android 는 Android 전용이고 iOS 에는 Activity 개념이 없다.
 */
@DependencyGraph(AppScope::class)
interface IosAppGraph : ViewModelGraph {
    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides buildFlags: BuildFlags,
        ): IosAppGraph
    }
}

/**
 * 프로세스 수명 동안 하나만 유지한다.
 *
 * `isDebug` 는 Kotlin 쪽에서 알 수 없다 — Xcode 의 빌드 구성(Debug/Release)이 정답이라
 * Swift 가 [createIosAppGraph] 로 넘겨준다.
 */
private var graph: IosAppGraph? = null

internal fun requireGraph(): IosAppGraph =
    checkNotNull(graph) {
        "IosAppGraph 가 초기화되지 않았습니다. Swift 에서 createIosAppGraph(isDebug:) 를 먼저 호출하세요."
    }

fun createIosAppGraph(isDebug: Boolean) {
    if (graph != null) return
    graph = createGraphFactory<IosAppGraph.Factory>().create(buildFlags = BuildFlags(isDebug = isDebug))
}
