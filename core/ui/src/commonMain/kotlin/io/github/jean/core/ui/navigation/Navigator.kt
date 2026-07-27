package io.github.jean.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * 앱 공용 네비게이터.
 *
 * 백스택을 소유하고 이동/뒤로가기를 제공한다.
 * [LocalNavigator]로 트리 어디서든 꺼내 쓸 수 있어, 화면마다 콜백을 넘길 필요가 없다.
 *
 * [backStack]은 [rememberNavBackStack] 기반이라 구성 변경/프로세스 사망을 넘어 보존된다.
 */
@Stable
class Navigator internal constructor(
    val backStack: NavBackStack<NavKey>,
) {
    /** 뒤로 갈 수 있는지 (루트 화면이 아닌지). */
    val canGoBack: Boolean
        get() = backStack.size > 1

    /** 새 화면을 스택에 쌓는다. */
    fun navigate(route: Route) {
        backStack.add(route)
    }

    /** 스택을 비우고 [route]만 남긴다. (예: 로그인 후 홈 진입) */
    fun navigateAndClear(route: Route) {
        backStack.clear()
        backStack.add(route)
    }

    /** 현재 최상단 화면을 [route]로 교체한다. (예: 저장 후 에디터를 상세로 대체) */
    fun replaceTop(route: Route) {
        if (backStack.isNotEmpty()) {
            backStack.removeAt(backStack.lastIndex)
        }
        backStack.add(route)
    }

    /**
     * 한 단계 뒤로 간다.
     * 루트 화면이면 아무 것도 하지 않고 false를 반환한다.
     */
    fun back(): Boolean {
        if (!canGoBack) return false
        backStack.removeAt(backStack.lastIndex)
        return true
    }
}

/**
 * [Route] 다형성 직렬화 설정.
 *
 * nav3 에는 설정 없이 쓰는 `rememberNavBackStack` 오버로드가 있지만 **Android 전용**이다
 * (리플렉션으로 서브타입을 찾는다). 공용 코드에서는 모든 [Route] 구현을 명시적으로 등록해야 한다.
 *
 * Route 는 feature 모듈마다 흩어져 있으므로, 전부를 아는 진입점(MainNavHost)에서 호출한다.
 * 등록을 빠뜨린 Route 는 백스택 복원 시점에 직렬화 예외로 드러난다.
 *
 * ```
 * val configuration = routeSavedStateConfiguration {
 *     subclass(HomeRoute::class)
 *     subclass(SearchRoute::class)
 * }
 * ```
 */
fun routeSavedStateConfiguration(register: PolymorphicModuleBuilder<NavKey>.() -> Unit): SavedStateConfiguration =
    SavedStateConfiguration {
        serializersModule =
            SerializersModule {
                polymorphic(NavKey::class, builderAction = register)
            }
    }

@Composable
fun rememberNavigator(
    startRoute: Route,
    configuration: SavedStateConfiguration,
): Navigator {
    val backStack = rememberNavBackStack(configuration, startRoute)
    return remember(backStack) { Navigator(backStack) }
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("Navigator가 제공되지 않았습니다. MainNavHost 안에서 사용하세요.")
}
