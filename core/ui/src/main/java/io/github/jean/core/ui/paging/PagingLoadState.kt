package io.github.jean.core.ui.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import io.github.jean.core.designsystem.component.LeafProgressIndicator
import io.github.jean.core.designsystem.component.LeafTextButton
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun PagingLoadingFooter(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth()) {
        LeafProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun PagingErrorFooter(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.error_title_for_footer),
            style = LeafTheme.typography.meta,
            color = LeafTheme.colors.textMuted,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(4.dp))

        LeafTextButton(
            text = stringResource(R.string.error_btn),
            typography = LeafTheme.typography.meta,
            leadingIconRes = LeafTheme.res.refresh,
            iconSize = 12.dp,
            onClick = onRefresh,
        )
    }
}

@ThemePreviews
@Composable
private fun PagingErrorFooterPreview() {
    LeafTheme {
        PagingErrorFooter(onRefresh = {})
    }
}
