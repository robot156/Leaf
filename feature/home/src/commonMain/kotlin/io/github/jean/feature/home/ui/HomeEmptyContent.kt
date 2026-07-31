package io.github.jean.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafMark
import io.github.jean.core.designsystem.component.LeafSolidButton
import io.github.jean.core.designsystem.generated.resources.Res
import io.github.jean.core.designsystem.generated.resources.home_empty_btn
import io.github.jean.core.designsystem.generated.resources.home_empty_desc
import io.github.jean.core.designsystem.generated.resources.home_empty_title
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.home.model.HomeIntent
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeEmptyContent(
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.weight(1f))

        LeafMark(
            iconSize = 56.dp,
            alpha = 0.35f,
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = stringResource(Res.string.home_empty_title),
            style = LeafTheme.typography.bookTitleSmall,
            color = LeafTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(Res.string.home_empty_desc),
            style = LeafTheme.typography.body,
            color = LeafTheme.colors.textMuted,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(28.dp))

        LeafSolidButton(
            text = stringResource(Res.string.home_empty_btn),
            onClick = { onIntent(HomeIntent.AddClick) },
            leadingIconRes = LeafTheme.res.plus,
        )

        Spacer(Modifier.weight(1.5f))
    }
}

@ThemePreviews
@Composable
private fun HomeEmptyContentPreview() {
    LeafTheme {
        HomeEmptyContent(onIntent = {})
    }
}
