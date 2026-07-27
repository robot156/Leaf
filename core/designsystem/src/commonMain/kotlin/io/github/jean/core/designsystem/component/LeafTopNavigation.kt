package io.github.jean.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.component.node.LeafTopNavItem
import io.github.jean.core.designsystem.theme.LeafTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun LeafTopNavigation(
    title: String,
    modifier: Modifier = Modifier,
    leading: LeafTopNavItem? = null,
    trailing: ImmutableList<LeafTopNavItem> = persistentListOf(),
    titleStyle: TextStyle = LeafTheme.typography.label,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        leading?.let { NavItem(it) }

        Text(
            text = title,
            style = titleStyle,
            color = LeafTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = if (leading == null) 12.dp else 2.dp),
        )

        trailing.forEach { NavItem(it) }
    }
}

@Composable
private fun NavItem(item: LeafTopNavItem) {
    when (item) {
        is LeafTopNavItem.Icon -> {
            LeafIconButton(
                icon = item.iconRes,
                contentDescription = item.contentDescription,
                onClick = item.onClick,
                enabled = item.enabled,
            )
        }

        is LeafTopNavItem.TextButton -> {
            LeafTextButton(
                text = item.text,
                onClick = item.onClick,
                enabled = item.enabled,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F4EF)
@Composable
private fun LeafTopNavigationRootPreview() {
    LeafTheme {
        LeafTopNavigation(
            title = "Leaf",
            titleStyle = LeafTheme.typography.wordmarkSmall,
            trailing =
                persistentListOf(
                    LeafTopNavItem.Icon(
                        iconRes = LeafTheme.res.menu,
                        contentDescription = "메뉴",
                        onClick = {},
                    ),
                ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F4EF)
@Composable
private fun LeafTopNavigationEditorPreview() {
    LeafTheme {
        LeafTopNavigation(
            title = "새 기록",
            leading =
                LeafTopNavItem.Icon(
                    iconRes = LeafTheme.res.chevronLeft,
                    contentDescription = "뒤로",
                    onClick = {},
                ),
            trailing =
                persistentListOf(
                    LeafTopNavItem.TextButton(
                        text = "저장",
                        onClick = {},
                        enabled = false,
                    ),
                ),
        )
    }
}
