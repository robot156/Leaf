package io.github.jean.feature.write.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.component.LeafTextButton
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.theme.LeafTheme
import io.github.jean.feature.write.search.model.SearchIntent
import io.github.jean.feature.write.search.model.section.SearchBookUiModel

@Composable
fun SearchListItem(
    uiModel: SearchBookUiModel,
    onIntent: (SearchIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = LeafTheme.colors

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .pressScale(
                    interactionSource = interactionSource,
                    pressedScale = 0.98f,
                ).clip(10.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = { onIntent(SearchIntent.BookClick(uiModel)) },
                ).padding(horizontal = 4.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LeafImage(
            modifier =
                Modifier
                    .width(42.dp)
                    .height(62.dp)
                    .clip(5.dp)
                    .border(width = 0.5.dp, color = colors.border, radius = 5.dp),
            model = uiModel.coverUrl,
            placeholder = LeafTheme.res.placeholder,
            scale = ContentScale.Crop,
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = uiModel.title,
                style = LeafTheme.typography.note,
                color = colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text =
                    listOf(uiModel.author, uiModel.publisher)
                        .filter { it.isNotBlank() }
                        .joinToString(" · "),
                style = LeafTheme.typography.meta,
                color = colors.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            DetailActionButton(
                onClick = {
                    onIntent(SearchIntent.BookDetailClick(uiModel))
                },
            )
        }
    }
}

@Composable
private fun DetailActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LeafTextButton(
        modifier = modifier,
        text = stringResource(R.string.search_book_detail),
        typography = LeafTheme.typography.meta,
        trailingIconRes = LeafTheme.res.chevronRight,
        iconSize = 8.dp,
        horizontalTouchSize = 0.dp,
        onClick = onClick,
    )
}

@ThemePreviews
@Composable
private fun SearchListItemPreview() {
    LeafTheme {
        SearchListItem(
            uiModel = SearchBookUiModel.Mock,
            onIntent = {},
        )
    }
}

@ThemePreviews
@Composable
private fun SearchListItemLongTitlePreview() {
    LeafTheme {
        SearchListItem(
            uiModel =
                SearchBookUiModel.Mock.copy(
                    title = "아주 길어서 두 줄을 넘어가 말줄임표가 필요한 제목의 검색 결과 케이스 — 부제까지 붙으면 이렇게 됩니다",
                    author = "저자명이 긴 경우 · 옮긴이까지 붙는 경우",
                ),
            onIntent = {},
        )
    }
}
