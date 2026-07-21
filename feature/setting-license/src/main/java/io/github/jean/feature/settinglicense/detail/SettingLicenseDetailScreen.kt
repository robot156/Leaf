package io.github.jean.feature.settinglicense.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafTopNavigation
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.settinglicense.detail.model.SettingLicenseDetailIntent
import io.github.jean.feature.settinglicense.detail.model.SettingLicenseDetailSideEffect
import io.github.jean.feature.settinglicense.detail.model.SettingLicenseDetailState
import io.github.jean.feature.settinglicense.detail.model.section.LicenseDetailUiModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingLicenseDetailRoute(
    uniqueId: String,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingLicenseDetailViewModel =
        assistedMetroViewModel<SettingLicenseDetailViewModel, SettingLicenseDetailViewModel.Factory> {
            create(uniqueId)
        },
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is SettingLicenseDetailSideEffect.NavigateToBack -> {
                navigateToBack()
            }
        }
    }

    SettingLicenseDetailScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun SettingLicenseDetailScreen(
    state: SettingLicenseDetailState,
    onIntent: (SettingLicenseDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LeafTheme.colors
    val license = state.license

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(colors.surface)
                .systemBarsPadding(),
    ) {
        LeafTopNavigation(
            title = license?.name ?: "",
            leading =
                LeafTopNavItem.Icon(
                    iconRes = LeafTheme.res.chevronLeft,
                    onClick = { onIntent(SettingLicenseDetailIntent.BackClick) },
                ),
        )

        if (license != null) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = license.licenseName,
                    style = LeafTheme.typography.label,
                    color = colors.textPrimary,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = license.author,
                    style = LeafTheme.typography.meta,
                    color = colors.textMuted,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = license.licenseContent,
                    style = LeafTheme.typography.meta,
                    color = colors.textSecondary,
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SettingLicenseDetailScreenPreview() {
    LeafTheme {
        SettingLicenseDetailScreen(
            state =
                SettingLicenseDetailState(
                    license =
                        LicenseDetailUiModel(
                            name = "리디바탕",
                            author = "RIDI Corporation",
                            licenseName = "SIL Open Font License 1.1",
                            licenseContent =
                                "SIL OPEN FONT LICENSE\n\n" +
                                    "Version 1.1 - 26 February 2007\n\n" +
                                    "PREAMBLE\n\n" +
                                    "The goals of the Open Font License (OFL) are to stimulate worldwide " +
                                    "development of collaborative font projects, to support the font creation " +
                                    "efforts of academic and linguistic communities, and to provide a free and " +
                                    "open framework in which fonts may be shared and improved in partnership " +
                                    "with others.",
                        ),
                ),
            onIntent = {},
        )
    }
}
