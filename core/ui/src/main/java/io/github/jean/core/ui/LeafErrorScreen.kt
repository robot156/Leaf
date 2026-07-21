package io.github.jean.core.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.component.LeafSolidButton
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafErrorScreen(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.weight(1.5f))
        if (showIcon) {
            LeafImage(
                model = LeafTheme.res.error,
                size = 56.dp,
            )

            Spacer(modifier = Modifier.height(22.dp))
        }

        Text(
            text = stringResource(R.string.error_title),
            style = LeafTheme.typography.bookTitleSmall,
            color = LeafTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.error_desc),
            style = LeafTheme.typography.body,
            color = LeafTheme.colors.textMuted,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(28.dp))

        LeafSolidButton(
            text = stringResource(R.string.error_btn),
            onClick = onRetry,
            leadingIconRes = LeafTheme.res.refresh,
        )

        Spacer(Modifier.weight(2f))
    }
}

@ThemePreviews
@Composable
private fun LeafErrorScreenPreview() {
    LeafTheme {
        LeafErrorScreen(onRetry = {})
    }
}
