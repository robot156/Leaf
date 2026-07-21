package io.github.jean.feature.note.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun NoteDetailQuoteBlock(
    page: Int?,
    sentence: String,
    modifier: Modifier = Modifier,
) {
    val colors = LeafTheme.colors

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .quoteLeftBorder(colors.primary)
                .padding(start = 14.dp, top = 3.dp, bottom = 3.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            LeafImage(
                model = LeafTheme.res.quote,
                color = colors.primaryMuted,
                size = 12.dp,
            )

            if (page != null) {
                Text(
                    text = stringResource(R.string.note_detail_page, page),
                    style = LeafTheme.typography.meta,
                    color = colors.primaryMuted,
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = sentence,
            style = LeafTheme.typography.quote,
            color = colors.textSecondary,
        )
    }
}

private fun Modifier.quoteLeftBorder(color: Color): Modifier =
    drawBehind {
        drawRect(
            color = color,
            size = Size(width = 2.dp.toPx(), height = size.height),
        )
    }

@ThemePreviews
@Composable
private fun NoteDetailQuoteBlockPreview() {
    LeafTheme {
        NoteDetailQuoteBlock(
            page = 10,
            sentence = "나는 내 안의 여럿과 함께 걷는다. 내가 나일 때조차, 나는 그들 중 하나일 뿐이다.",
        )
    }
}
