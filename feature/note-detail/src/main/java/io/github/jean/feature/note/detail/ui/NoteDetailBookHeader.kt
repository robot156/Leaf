package io.github.jean.feature.note.detail.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun NoteDetailBookHeader(
    title: String,
    author: String,
    coverUrl: String,
    recordedDateText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = LeafTheme.colors

    Column(
        modifier =
            modifier
                .pressScale(
                    interactionSource = interactionSource,
                    pressedScale = 0.97f,
                ).clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onClick,
                ).padding(horizontal = 20.dp)
                .padding(bottom = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LeafImage(
                model = coverUrl,
                contentDescription = null,
                scale = ContentScale.Crop,
                modifier =
                    Modifier
                        .width(48.dp)
                        .height(70.dp)
                        .clip(6.dp)
                        .border(width = 0.5.dp, color = colors.border, radius = 6.dp),
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = LeafTheme.typography.bookTitleSmall,
                    color = colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = author,
                    style = LeafTheme.typography.meta,
                    color = colors.textMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = recordedDateText,
            style = LeafTheme.typography.meta,
            color = colors.textMuted,
        )
    }

    HorizontalDivider(thickness = 0.5.dp, color = LeafTheme.colors.border)
}

@ThemePreviews
@Composable
private fun NoteDetailBookHeaderPreview() {
    LeafTheme {
        NoteDetailBookHeader(
            modifier = Modifier.padding(top = 10.dp),
            title = "소년이 온다",
            author = "한강",
            coverUrl = "",
            recordedDateText = "2026년 7월 12일의 기록",
            onClick = {},
        )
    }
}
