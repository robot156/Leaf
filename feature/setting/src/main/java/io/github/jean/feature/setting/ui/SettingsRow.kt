package io.github.jean.feature.setting.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.component.LeafImage
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.modifier.throttleClickable
import io.github.jean.core.designsystem.theme.LeafTheme

/**
 * 설정 행.
 *
 * 우측 구성이 행의 성격을 말한다:
 * - value + chevron: 이동해서 바꾸는 설정 (테마)
 * - chevron만: 이동 (라이선스, 문의)
 * - value만 + onClick: 즉시 실행 (캐시 삭제)
 * - value만 + onClick 없음: 표시 전용 (앱 버전)
 */
@Composable
fun SettingsRow(
    label: String,
    modifier: Modifier = Modifier,
    value: String? = null,
    showChevron: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = LeafTheme.colors

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .pressScale(
                    interactionSource = interactionSource,
                    enabled = onClick != null,
                    pressedScale = 0.98f,
                )
                .then(
                    if (onClick != null) {
                        Modifier.throttleClickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Button,
                            onClick = onClick,
                        )
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = 20.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = LeafTheme.typography.body,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (value != null) {
                Text(
                    text = value,
                    style = LeafTheme.typography.meta,
                    color = colors.textMuted,
                )
            }

            if (showChevron) {
                LeafImage(
                    model = LeafTheme.res.chevronRight,
                    color = colors.textMuted,
                    size = 12.dp,
                )
            }
        }
    }
}
