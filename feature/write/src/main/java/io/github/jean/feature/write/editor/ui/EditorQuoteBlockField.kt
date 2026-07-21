package io.github.jean.feature.write.editor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafIconButton
import io.github.jean.core.designsystem.component.LeafNoteTextField
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.write.editor.model.EditorIntent
import io.github.jean.feature.write.editor.model.section.EditorBlock

@Composable
fun EditorQuoteBlockField(
    block: EditorBlock.Quote,
    requestFocus: Boolean,
    modifier: Modifier = Modifier,
    onIntent: (EditorIntent) -> Unit,
) {
    val colors = LeafTheme.colors
    val focusRequester = remember { FocusRequester() }
    if (requestFocus) {
        LaunchedEffect(block.id) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .quoteLeftBorder(colors.primary)
                .padding(start = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.editor_quote_prefix),
                    style = LeafTheme.typography.meta,
                    color = colors.primaryMuted,
                )
                PageField(
                    page = block.page,
                    onPageChange = { onIntent(EditorIntent.QuotePageChanged(block.id, it)) },
                )
            }

            LeafIconButton(
                icon = LeafTheme.res.close,
                tint = colors.textMuted,
                iconSize = 20.dp,
                onClick = { onIntent(EditorIntent.QuoteRemoveClick(block.id)) },
            )
        }

        var sentence by remember(block.id) {
            mutableStateOf(TextFieldValue(block.sentence, TextRange(block.sentence.length)))
        }
        // Record 필드와 동일 이유: recomposition마다 비교하면 커서가 밀린다. VM 주도 변경에만 반영.
        LaunchedEffect(block.sentence) {
            if (block.sentence != sentence.text) {
                sentence = TextFieldValue(block.sentence, TextRange(block.sentence.length))
            }
        }

        LeafNoteTextField(
            value = sentence,
            onValueChange = { new ->
                sentence = new
                if (new.text != block.sentence) onIntent(EditorIntent.QuoteSentenceChanged(block.id, new.text))
            },
            placeholder = stringResource(R.string.editor_quote_placeholder),
            textStyle = LeafTheme.typography.quote,
            color = colors.textSecondary,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .focusRequester(focusRequester),
        )
    }
}

@Composable
private fun PageField(
    page: Int?,
    onPageChange: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf(page?.toString().orEmpty()) }
    if (page?.toString().orEmpty() != text && page != text.toIntOrNull()) {
        text = page?.toString().orEmpty()
    }
    val colors = LeafTheme.colors

    BasicTextField(
        value = text,
        onValueChange = { new ->
            val filtered = new.filter(Char::isDigit).take(4)
            text = filtered
            onPageChange(filtered.toIntOrNull())
        },
        textStyle = LeafTheme.typography.meta.copy(color = colors.textPrimary),
        cursorBrush = SolidColor(colors.primary),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier =
            modifier
                .width(36.dp)
                .clip(6.dp)
                .background(colors.card)
                .border(width = 0.5.dp, color = colors.border, radius = 6.dp)
                .padding(horizontal = 4.dp, vertical = 3.dp),
        decorationBox = { inner ->
            Box(contentAlignment = Alignment.Center) {
                if (text.isEmpty()) {
                    Text(text = "—", style = LeafTheme.typography.meta, color = colors.textMuted)
                }
                inner()
            }
        },
    )
}

/** 인용 블록 왼쪽 세로선 */
private fun Modifier.quoteLeftBorder(color: Color): Modifier =
    drawBehind {
        drawRect(
            color = color,
            size =
                androidx.compose.ui.geometry
                    .Size(2.dp.toPx(), size.height),
        )
    }.let { this.then(it) }

@ThemePreviews
@Composable
private fun EditorQuoteBlockFieldPreview() {
    LeafTheme {
        EditorQuoteBlockField(
            modifier = Modifier.padding(4.dp),
            block = EditorBlock.Quote(id = 0, page = 1, sentence = ""),
            requestFocus = true,
            onIntent = {},
        )
    }
}
