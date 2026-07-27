package io.github.jean.feature.main

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.jean.core.common.model.Theme
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.main.navigation.MainNavHost
import org.orbitmvi.orbit.compose.collectAsState

@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey
class MainActivity(
    private val metroVmf: MetroViewModelFactory,
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
                val viewModel: MainViewModel = metroViewModel()
                val state by viewModel.collectAsState()
                val isDarkTheme =
                    when (state.theme) {
                        Theme.Light -> false
                        Theme.Dark -> true
                        Theme.System -> isSystemInDarkTheme()
                    }

                LaunchedEffect(isDarkTheme) {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { isDarkTheme },
                        navigationBarStyle = SystemBarStyle.auto(lightScrim, darkScrim) { isDarkTheme },
                    )
                }

                LeafTheme(
                    palette = state.palette,
                    isDarkTheme = isDarkTheme,
                ) {
                    MainNavHost()
                }
            }
        }
    }
}

private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
