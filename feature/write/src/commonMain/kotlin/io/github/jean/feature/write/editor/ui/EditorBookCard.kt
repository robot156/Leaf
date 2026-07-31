package io.github.jean.feature.write.editor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.write.editor.model.EditorIntent
import io.github.jean.feature.write.editor.model.section.EditorBookUiModel

@Composable
fun EditorBookCard(
    book: EditorBookUiModel,
    onIntent: (EditorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = LeafTheme.colors

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .pressScale(interactionSource = interactionSource, pressedScale = 0.98f)
                .clip(12.dp)
                .background(colors.card)
                .border(width = 0.5.dp, color = colors.border, radius = 12.dp)
                .throttleClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = { onIntent(EditorIntent.BookChoiceClick) },
                ).padding(horizontal = 12.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LeafImage(
            modifier =
                Modifier
                    .width(22.dp)
                    .height(32.dp)
                    .clip(4.dp),
            model = book.coverUrl ?: "",
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.title,
                style = LeafTheme.typography.bookTitleSmall,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = book.author,
                style = LeafTheme.typography.meta,
                color = colors.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun EditorBookCardPreview() {
    LeafTheme {
        val book =
            EditorBookUiModel(
                bookId = 1L,
                title = "페르난두 페소아 — 페소아와 페소아들",
                author = "김한민 엮고 옮김",
                coverUrl = null,
            )

        EditorBookCard(
            modifier = Modifier.padding(12.dp),
            book = book,
            onIntent = {},
        )
    }
}
