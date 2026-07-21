package io.github.jean.feature.note.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafBottomSheet
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.component.LeafSolidButton
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.note.detail.model.NoteDetailIntent
import io.github.jean.feature.note.detail.model.section.NoteDetailBookUiModel

@Composable
fun NoteDetailBookDetailBottomSheet(
    uiModel: NoteDetailBookUiModel,
    onIntent: (NoteDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LeafBottomSheet(
        modifier = modifier,
        content = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LeafImage(
                    modifier =
                        Modifier
                            .width(96.dp)
                            .height(140.dp)
                            .clip(8.dp)
                            .border(width = 0.5.dp, color = LeafTheme.colors.border, radius = 8.dp),
                    model = uiModel.coverUrl,
                    scale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = uiModel.title,
                    style = LeafTheme.typography.bookTitleSmall,
                    color = LeafTheme.colors.textPrimary,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = uiModel.author,
                    style = LeafTheme.typography.meta,
                    color = LeafTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text =
                        listOfNotNull(
                            uiModel.publisher.takeIf { it.isNotBlank() },
                            uiModel.isbn13
                                .takeIf { it.isNotBlank() }
                                ?.let { stringResource(R.string.note_detail_book_isbn, it) },
                        ).joinToString(" · "),
                    style = LeafTheme.typography.meta,
                    color = LeafTheme.colors.textMuted,
                    textAlign = TextAlign.Center,
                )

                if (uiModel.description.isNotBlank()) {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = LeafTheme.colors.border,
                        modifier = Modifier.padding(top = 18.dp, bottom = 14.dp),
                    )

                    Text(
                        text = stringResource(R.string.note_detail_book_description),
                        style = LeafTheme.typography.label,
                        color = LeafTheme.colors.textMuted,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                    )
                    Text(
                        text = uiModel.description,
                        style = LeafTheme.typography.quote,
                        color = LeafTheme.colors.textSecondary,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        bottomBar =
            if (uiModel.link.isNotBlank()) {
                {
                    LeafSolidButton(
                        text = stringResource(R.string.note_detail_book_external_link),
                        onClick = { onIntent(NoteDetailIntent.ExternalWebSiteClick(uiModel.link)) },
                        fillMaxWidth = true,
                    )
                }
            } else {
                null
            },
        onDismiss = {
            onIntent(NoteDetailIntent.BookDetailDialogHide)
        },
    )
}

@ThemePreviews
@Composable
private fun NoteDetailBookDetailBottomSheetPreview() {
    LeafTheme {
        NoteDetailBookDetailBottomSheet(
            uiModel = NoteDetailBookUiModel.Mock,
            onIntent = {},
        )
    }
}
