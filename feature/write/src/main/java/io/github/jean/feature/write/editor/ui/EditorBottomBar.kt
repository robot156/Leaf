package io.github.jean.feature.write.editor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafOutlinedButton
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.write.editor.model.EditorIntent

@Composable
fun EditorBottomBar(
    hint: String?,
    onIntent: (EditorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LeafTheme.colors
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(thickness = 0.5.dp, color = colors.border)
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(colors.card)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LeafOutlinedButton(
                text = stringResource(R.string.editor_quote_add),
                onClick = { onIntent(EditorIntent.QuoteAddClick) },
                leadingIconRes = LeafTheme.res.quote,
            )
            if (hint != null) {
                Text(
                    text = hint,
                    style = LeafTheme.typography.meta,
                    color = colors.textMuted,
                    modifier = Modifier.padding(start = 12.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun EditorBottomBarPreview() {
    LeafTheme {
        EditorBottomBar(
            hint = stringResource(R.string.editor_char_count, 62),
            onIntent = {},
        )
    }
}
