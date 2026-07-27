package io.github.jean.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.jean.core.common.model.Theme
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafAlertDialog
import io.github.jean.core.designsystem.component.LeafTopNavigation
import io.github.jean.core.designsystem.component.node.LeafDialogButton
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.generated.resources.Res
import io.github.jean.core.designsystem.generated.resources.setting_app_version
import io.github.jean.core.designsystem.generated.resources.setting_back
import io.github.jean.core.designsystem.generated.resources.setting_clear_cache
import io.github.jean.core.designsystem.generated.resources.setting_clear_cache_dialog_cancel
import io.github.jean.core.designsystem.generated.resources.setting_clear_cache_dialog_confirm
import io.github.jean.core.designsystem.generated.resources.setting_clear_cache_dialog_description
import io.github.jean.core.designsystem.generated.resources.setting_clear_cache_dialog_title
import io.github.jean.core.designsystem.generated.resources.setting_contact
import io.github.jean.core.designsystem.generated.resources.setting_licenses
import io.github.jean.core.designsystem.generated.resources.setting_section_data
import io.github.jean.core.designsystem.generated.resources.setting_section_display
import io.github.jean.core.designsystem.generated.resources.setting_section_info
import io.github.jean.core.designsystem.generated.resources.setting_theme
import io.github.jean.core.designsystem.generated.resources.setting_theme_dark
import io.github.jean.core.designsystem.generated.resources.setting_theme_light
import io.github.jean.core.designsystem.generated.resources.setting_theme_system
import io.github.jean.core.designsystem.generated.resources.setting_title
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.setting.model.SettingIntent
import io.github.jean.feature.setting.model.SettingSideEffect
import io.github.jean.feature.setting.model.SettingState
import io.github.jean.feature.setting.ui.SettingsRow
import io.github.jean.feature.setting.ui.SettingsSectionHeader
import org.jetbrains.compose.resources.stringResource
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingRoute(
    navigateToBack: () -> Unit,
    navigateToThemeSetting: () -> Unit,
    navigateToEmail: (
        appVersion: String,
        osVersion: String,
        deviceModel: String,
    ) -> Unit,
    navigateToLicenses: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = metroViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is SettingSideEffect.NavigateToBack -> {
                navigateToBack()
            }

            is SettingSideEffect.NavigateToThemeSetting -> {
                navigateToThemeSetting()
            }

            is SettingSideEffect.NavigateToEmail -> {
                navigateToEmail(effect.appVersion, effect.osVersion, effect.deviceModel)
            }

            is SettingSideEffect.NavigateToLicenses -> {
                navigateToLicenses()
            }
        }
    }

    SettingScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun SettingScreen(
    state: SettingState,
    onIntent: (SettingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LeafTheme.colors
    val themeLabel =
        when (state.theme) {
            Theme.Light -> stringResource(Res.string.setting_theme_light)
            Theme.Dark -> stringResource(Res.string.setting_theme_dark)
            Theme.System -> stringResource(Res.string.setting_theme_system)
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(colors.surface)
                .systemBarsPadding(),
    ) {
        LeafTopNavigation(
            title = stringResource(Res.string.setting_title),
            leading =
                LeafTopNavItem.Icon(
                    iconRes = LeafTheme.res.chevronLeft,
                    contentDescription = stringResource(Res.string.setting_back),
                    onClick = { onIntent(SettingIntent.BackClick) },
                ),
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
        ) {
            SettingsSectionHeader(text = stringResource(Res.string.setting_section_display))
            SettingsRow(
                label = stringResource(Res.string.setting_theme),
                value = themeLabel,
                showChevron = true,
                onClick = { onIntent(SettingIntent.ThemeClick) },
            )

            SettingsSectionHeader(text = stringResource(Res.string.setting_section_data))
            SettingsRow(
                label = stringResource(Res.string.setting_clear_cache),
                onClick = { onIntent(SettingIntent.ClearCacheClick) },
            )

            SettingsSectionHeader(text = stringResource(Res.string.setting_section_info))
            SettingsRow(
                label = stringResource(Res.string.setting_contact),
                showChevron = true,
                onClick = { onIntent(SettingIntent.ContactClick) },
            )
            SettingsRow(
                label = stringResource(Res.string.setting_licenses),
                showChevron = true,
                onClick = { onIntent(SettingIntent.LicensesClick) },
            )
            SettingsRow(
                label = stringResource(Res.string.setting_app_version),
                value = state.appVersion,
            )
        }
    }

    if (state.isShowClearCacheDialog) {
        LeafAlertDialog(
            onDismiss = { onIntent(SettingIntent.ClearCacheDialogDismiss) },
            title = stringResource(Res.string.setting_clear_cache_dialog_title),
            description = stringResource(Res.string.setting_clear_cache_dialog_description),
            negativeButton =
                LeafDialogButton(
                    text = stringResource(Res.string.setting_clear_cache_dialog_cancel),
                    onClick = { onIntent(SettingIntent.ClearCacheDialogDismiss) },
                ),
            positiveButton =
                LeafDialogButton(
                    text = stringResource(Res.string.setting_clear_cache_dialog_confirm),
                    onClick = { onIntent(SettingIntent.ClearCacheConfirm) },
                ),
        )
    }
}

@ThemePreviews
@Composable
private fun SettingScreenPreview() {
    LeafTheme {
        SettingScreen(
            state =
                SettingState(
                    theme = Theme.System,
                    appVersion = "2026.07.01",
                ),
            onIntent = {},
        )
    }
}
