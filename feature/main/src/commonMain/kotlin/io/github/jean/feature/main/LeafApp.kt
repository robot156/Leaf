package io.github.jean.feature.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.jean.core.common.model.Theme
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.main.navigation.MainNavHost
import org.orbitmvi.orbit.compose.collectAsState

/**
 * 앱 컴포지션 루트. Android 는 `MainActivity` 의 `setContent`, iOS 는
 * `ComposeUIViewController` 안에서 이걸 호출한다.
 *
 * @param onDarkThemeChanged 시스템 바 색 같은 플랫폼 크롬을 테마에 맞추기 위한 훅.
 *   Android 는 `enableEdgeToEdge` 를 다시 호출한다. iOS 는 할 일이 없어 기본값을 쓴다.
 */
@Composable
fun LeafApp(
    viewModelFactory: MetroViewModelFactory,
    onDarkThemeChanged: (isDarkTheme: Boolean) -> Unit = {},
) {
    CompositionLocalProvider(LocalMetroViewModelFactory provides viewModelFactory) {
        val viewModel: MainViewModel = metroViewModel()
        val state by viewModel.collectAsState()
        val isDarkTheme =
            when (state.theme) {
                Theme.Light -> false
                Theme.Dark -> true
                Theme.System -> isSystemInDarkTheme()
            }

        LaunchedEffect(isDarkTheme) {
            onDarkThemeChanged(isDarkTheme)
        }

        LeafTheme(
            palette = state.palette,
            isDarkTheme = isDarkTheme,
        ) {
            MainNavHost()
        }
    }
}
