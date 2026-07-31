package io.github.jean.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.node.LeafDialogButton
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.theme.LeafTheme

/**
 * Leaf 알럿 다이얼로그.
 *
 * - [title], [description]은 각각 생략 가능 (단 둘 다 없을 수는 없다)
 * - 버튼이 둘이면 좌(negative)/우(positive) 반반, 하나면 좌우 꽉 채움
 * - positive = 솔리드(주 동작), negative = 아웃라인(보조/취소) 위계
 */
@Composable
fun LeafAlertDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    positiveButton: LeafDialogButton? = null,
    negativeButton: LeafDialogButton? = null,
) {
    require(title != null || description != null) {
        "title과 description 중 최소 하나는 있어야 합니다"
    }
    require(positiveButton != null || negativeButton != null) {
        "positiveButton과 negativeButton 중 최소 하나는 있어야 합니다"
    }

    if (LocalInspectionMode.current) {
        Box(
            modifier = Modifier.padding(24.dp),
        ) {
            LeafAlertDialogContent(
                modifier = modifier,
                title = title,
                description = description,
                positiveButton = positiveButton,
                negativeButton = negativeButton,
            )
        }
        return
    }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        LeafTheme(
            isDarkTheme = LeafTheme.isDarkTheme,
            palette = LeafTheme.palette,
        ) {
            LeafAlertDialogContent(
                modifier = modifier,
                title = title,
                description = description,
                positiveButton = positiveButton,
                negativeButton = negativeButton,
            )
        }
    }
}

@Composable
private fun LeafAlertDialogContent(
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    positiveButton: LeafDialogButton? = null,
    negativeButton: LeafDialogButton? = null,
) {
    val colors = LeafTheme.colors

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(20.dp)
                .background(colors.card)
                .padding(24.dp),
    ) {
        if (title != null) {
            Text(
                text = title,
                style = LeafTheme.typography.bookTitleSmall,
                color = colors.textPrimary,
            )
        }

        if (description != null) {
            if (title != null) {
                Spacer(modifier = Modifier.height(10.dp))
            }
            Text(
                text = description,
                style = LeafTheme.typography.body,
                color = colors.textSecondary,
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (negativeButton != null) {
                LeafOutlinedButton(
                    text = negativeButton.text,
                    onClick = negativeButton.onClick,
                    modifier = Modifier.weight(1f),
                )
            }
            if (positiveButton != null) {
                LeafSolidButton(
                    text = positiveButton.text,
                    onClick = positiveButton.onClick,
                    fillMaxWidth = true,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun LeafAlertDialogPreview() {
    LeafTheme {
        LeafAlertDialog(
            onDismiss = {},
            title = "기록을 삭제할까요?",
            description = "삭제한 기록은 되돌릴 수 없어요.",
            negativeButton = LeafDialogButton(text = "취소", onClick = {}),
            positiveButton = LeafDialogButton(text = "삭제", onClick = {}),
        )
    }
}

@ThemePreviews
@Composable
private fun LeafAlertDialogSingleButtonPreview() {
    LeafTheme {
        LeafAlertDialog(
            onDismiss = {},
            title = "캐시를 청소했어요",
            description = "이제 데이터가 새롭게 로드 됩니다",
            positiveButton = LeafDialogButton(text = "확인", onClick = {}),
        )
    }
}
