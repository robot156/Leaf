package io.github.jean.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.home.model.HomeIntent
import io.github.jean.feature.home.model.section.HomeBookUiModel

@Composable
fun HomeListItem(
    model: HomeBookUiModel,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = LeafTheme.colors

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .pressScale(
                    interactionSource = interactionSource,
                    pressedScale = 0.97f,
                ).clip(12.dp)
                .background(colors.card)
                .border(
                    width = 0.5.dp,
                    color = colors.border,
                    radius = 12.dp,
                ).throttleClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = { onIntent(HomeIntent.BookNoteClick(model.noteId)) },
                ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(colors.primary),
        )

        Column(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 14.dp, bottom = 12.dp),
        ) {
            Text(
                text = model.title,
                style = LeafTheme.typography.bookTitleSmall,
                color = colors.textPrimary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                thickness = 0.5.dp,
                color = colors.border,
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = model.author,
                    style = LeafTheme.typography.meta,
                    color = colors.textMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = model.lastEditedDate,
                    style = LeafTheme.typography.meta,
                    color = colors.textMuted,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun HomeListItemPreview() {
    LeafTheme {
        HomeListItem(
            modifier = Modifier.padding(12.dp),
            model =
                HomeBookUiModel(
                    noteId = 1L,
                    title = "불안의 서",
                    author = "페르난두 페소아",
                    lastEditedDate = "2026.06.28",
                ),
            onIntent = {},
        )
    }
}
