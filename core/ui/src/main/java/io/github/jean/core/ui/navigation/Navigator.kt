package io.github.jean.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

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

@Composable
fun rememberNavigator(startRoute: Route): Navigator {
    val backStack = rememberNavBackStack(startRoute)
    return remember(backStack) { Navigator(backStack) }
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("Navigator가 제공되지 않았습니다. MainNavHost 안에서 사용하세요.")
}
