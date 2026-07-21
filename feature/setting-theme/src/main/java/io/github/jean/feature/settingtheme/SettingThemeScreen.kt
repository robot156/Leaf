package io.github.jean.feature.settingtheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.jean.core.common.model.Theme
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.component.LeafTopNavigation
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.settingtheme.model.SettingThemeIntent
import io.github.jean.feature.settingtheme.model.SettingThemeSideEffect
import io.github.jean.feature.settingtheme.model.SettingThemeState
import io.github.jean.feature.settingtheme.ui.SettingThemeHero
import io.github.jean.feature.settingtheme.ui.SettingThemeModeRow
import io.github.jean.feature.settingtheme.ui.SettingThemePaletteRow
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingThemeRoute(
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingThemeViewModel = metroViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is SettingThemeSideEffect.NavigateToBack -> {
                navigateToBack()
            }
        }
    }

    SettingThemeScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun SettingThemeScreen(
    state: SettingThemeState,
    onIntent: (SettingThemeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(LeafTheme.colors.surface)
                .systemBarsPadding(),
    ) {
        LeafTopNavigation(
            title = stringResource(R.string.setting_theme),
            leading =
                LeafTopNavItem.Icon(
                    iconRes = LeafTheme.res.chevronLeft,
                    onClick = { onIntent(SettingThemeIntent.BackClick) },
                ),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            SettingThemeHero(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(18.dp))

            SettingThemePaletteRow(
                currentPalette = state.currentPalette,
                onPaletteSelect = { onIntent(SettingThemeIntent.PaletteSelect(it)) },
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.setting_theme_section_brightness),
                style = LeafTheme.typography.label,
                color = LeafTheme.colors.textMuted,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Theme.entries.forEach { theme ->
                SettingThemeModeRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    theme = theme,
                    selected = theme == state.currentTheme,
                    onClick = { onIntent(SettingThemeIntent.ThemeSelect(theme)) },
                )
            }
        }
    }
}
