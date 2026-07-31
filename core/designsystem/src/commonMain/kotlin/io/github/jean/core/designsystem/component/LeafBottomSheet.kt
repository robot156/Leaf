package io.github.jean.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    showHandle: Boolean = true,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LeafTheme.colors

    if (LocalInspectionMode.current) {
        LeafBottomSheetPreviewFrame(
            showHandle = showHandle,
            bottomBar = bottomBar,
            content = content,
        )
        return
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = colors.card,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle =
            if (showHandle) {
                { LeafDragHandle() }
            } else {
                null
            },
    ) {
        LeafTheme(
            isDarkTheme = LeafTheme.isDarkTheme,
            palette = LeafTheme.palette,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()

                if (bottomBar != null) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 12.dp, bottom = 12.dp)
                                .navigationBarsPadding(),
                    ) {
                        bottomBar()
                    }
                }
            }
        }
    }
}

/** 프리뷰 전용 — LeafBottomSheet의 비주얼을 윈도우 없이 재현한다 */
@Composable
private fun LeafBottomSheetPreviewFrame(
    modifier: Modifier = Modifier,
    showHandle: Boolean = true,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(20.dp)
                .background(LeafTheme.colors.card),
    ) {
        if (showHandle) LeafDragHandle()

        content()

        if (bottomBar != null) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp, bottom = 12.dp)
                        .navigationBarsPadding(),
            ) {
                bottomBar()
            }
        }
    }
}

@Composable
private fun LeafDragHandle(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .clip(2.dp)
                    .background(LeafTheme.colors.border),
        )
    }
}

@ThemePreviews
@Composable
private fun LeafBottomSheetPreview() {
    LeafTheme {
        LeafBottomSheet(
            bottomBar = {
                LeafSolidButton(
                    text = "적용",
                    onClick = {},
                    fillMaxWidth = true,
                )
            },
            onDismiss = {},
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "정렬 기준",
                    style = LeafTheme.typography.bookTitle,
                    color = LeafTheme.colors.textPrimary,
                )
                Text(
                    text = "최근 읽은 순",
                    style = LeafTheme.typography.body,
                    color = LeafTheme.colors.textSecondary,
                )
                Text(
                    text = "제목순",
                    style = LeafTheme.typography.body,
                    color = LeafTheme.colors.textSecondary,
                )
            }
        }
    }
}
