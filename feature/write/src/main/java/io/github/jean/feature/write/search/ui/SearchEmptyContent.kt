package io.github.jean.feature.write.search.ui

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
import io.github.jean.core.designsystem.component.LeafMark
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun SearchEmptyContent(
    keyword: String,
    modifier: Modifier = Modifier,
) {
    val (title, desc) =
        if (keyword.isNotBlank()) {
            stringResource(R.string.search_empty_title) to stringResource(R.string.search_empty_desc)
        } else {
            stringResource(R.string.search_start_title) to stringResource(R.string.search_start_desc)
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.weight(1.5f))

        LeafMark(
            iconSize = 56.dp,
            alpha = 0.35f
        )

        Text(
            text = title,
            style = LeafTheme.typography.bookTitleSmall,
            color = LeafTheme.colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = desc,
            style = LeafTheme.typography.body,
            color = LeafTheme.colors.textMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.weight(2f))
    }
}

@ThemePreviews
@Composable
private fun SearchEmptyContentPreview() {
    LeafTheme {
        SearchEmptyContent(keyword = "책")
    }
}

@ThemePreviews
@Composable
private fun SearchEmptyContentStartPreview() {
    LeafTheme {
        SearchEmptyContent(keyword = "")
    }
}
