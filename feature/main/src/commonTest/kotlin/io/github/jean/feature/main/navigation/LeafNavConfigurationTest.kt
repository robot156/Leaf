package io.github.jean.feature.main.navigation

import androidx.navigation3.runtime.NavKey
import io.github.jean.core.ui.navigation.Route
import io.github.jean.feature.home.navigation.HomeRoute
import io.github.jean.feature.imageviewer.navigation.ImageViewerRoute
import io.github.jean.feature.intro.navigation.IntroRoute
import io.github.jean.feature.note.detail.navigation.NoteDetailRoute
import io.github.jean.feature.setting.navigation.SettingRoute
import io.github.jean.feature.settinglicense.navigation.SettingLicenseDetailRoute
import io.github.jean.feature.settinglicense.navigation.SettingLicensesRoute
import io.github.jean.feature.settingtheme.navigation.SettingThemeRoute
import io.github.jean.feature.write.navigation.EditorRoute
import io.github.jean.feature.write.navigation.SearchRoute
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * nav3 의 백스택 직렬화는 [LeafNavConfiguration] 에 등록된 Route 만 복원할 수 있다.
 * (설정 없는 오버로드는 리플렉션 기반이라 Android 전용이어서 쓸 수 없다)
 *
 * 등록을 빠뜨리면 **컴파일은 통과하고 프로세스 사망 후 복원 시점에만** 터진다.
 * 그 실패를 빌드 시점으로 끌어오는 테스트다.
 *
 * 새 Route 를 추가하면 [allRoutes] 에도 추가해야 한다.
 */
class LeafNavConfigurationTest {
    private val allRoutes: List<Route> =
        listOf(
            IntroRoute,
            HomeRoute,
            SearchRoute,
            EditorRoute(),
            EditorRoute(noteId = 1L),
            NoteDetailRoute(noteId = 1L),
            ImageViewerRoute(imageUrl = "https://example.com/a.png"),
            SettingRoute,
            SettingThemeRoute,
            SettingLicensesRoute,
            SettingLicenseDetailRoute(uniqueId = "some-id"),
        )

    @Test
    fun `모든 Route 가 다형성 직렬화기를 찾을 수 있다`() {
        val module = LeafNavConfiguration.serializersModule

        allRoutes.forEach { route ->
            assertNotNull(
                module.getPolymorphic(baseClass = NavKey::class, value = route),
                message = "$route 가 LeafNavConfiguration 에 등록되지 않았다. MainNavHost 의 등록 목록에 추가할 것.",
            )
        }
    }
}
