package io.github.jean.feature.settinglicense.list

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafTopNavigation
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.settinglicense.list.model.SettingLicensesIntent
import io.github.jean.feature.settinglicense.list.model.SettingLicensesSideEffect
import io.github.jean.feature.settinglicense.list.model.SettingLicensesState
import io.github.jean.feature.settinglicense.list.model.section.LicenseUiModel
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingLicensesRoute(
    navigateToBack: () -> Unit,
    navigateToLicenseDetail: (uniqueId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingLicensesViewModel = metroViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is SettingLicensesSideEffect.NavigateToBack -> {
                navigateToBack()
            }

            is SettingLicensesSideEffect.NavigateToLicenseDetail -> {
                navigateToLicenseDetail(effect.uniqueId)
            }
        }
    }

    SettingLicenseScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun SettingLicenseScreen(
    state: SettingLicensesState,
    onIntent: (SettingLicensesIntent) -> Unit,
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
            title = stringResource(R.string.setting_licenses),
            leading =
                LeafTopNavItem.Icon(
                    iconRes = LeafTheme.res.chevronLeft,
                    onClick = { onIntent(SettingLicensesIntent.BackClick) },
                ),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = state.licenses,
                key = { it.uniqueId },
            ) { item ->
                LicenseCard(
                    item = item,
                    onClick = { onIntent(SettingLicensesIntent.CardClick(item.uniqueId)) },
                )
            }
        }
    }
}

@Composable
private fun LicenseCard(
    item: LicenseUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LeafTheme.colors
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .pressScale(interactionSource = interactionSource, pressedScale = 0.98f)
                .clip(14.dp)
                .background(colors.surfaceDim)
                .throttleClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.name,
                style = LeafTheme.typography.label,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.version ?: "",
                style = LeafTheme.typography.meta,
                color = colors.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.author,
            style = LeafTheme.typography.meta,
            color = colors.textMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = item.licenseName,
            style = LeafTheme.typography.meta,
            color = colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@ThemePreviews
@Composable
private fun SettingLicenseScreenPreview() {
    LeafTheme {
        SettingLicenseScreen(
            state =
                SettingLicensesState(
                    licenses =
                        persistentListOf(
                            LicenseUiModel(
                                uniqueId = "androidx.compose.runtime:runtime",
                                name = "Compose Runtime",
                                version = "1.7.6",
                                author = "The Android Open Source Project",
                                licenseName = "Apache License 2.0",
                            ),
                            LicenseUiModel(
                                uniqueId = "org.jetbrains.kotlinx:kotlinx-coroutines-core",
                                name = "kotlinx-coroutines-core",
                                version = "1.9.0",
                                author = "JetBrains",
                                licenseName = "Apache License 2.0",
                            ),
                            LicenseUiModel(
                                uniqueId = "com.mikepenz:aboutlibraries-core",
                                name = "AboutLibraries Core",
                                version = null,
                                author = "Mike Penz",
                                licenseName = "Apache License 2.0",
                            ),
                        ),
                ),
            onIntent = {},
        )
    }
}
