package io.github.jean.feature.write.search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.modifier.skeletonPulse
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun SearchSkeletonContent(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
    ) {
        repeat(SKELETON_COUNT) {
            SearchListItemSkeleton()
        }
    }
}

@Composable
private fun SearchListItemSkeleton(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .width(42.dp)
                    .height(62.dp)
                    .skeletonPulse(5.dp),
        )

        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .height(14.dp)
                        .skeletonPulse(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(12.dp)
                        .skeletonPulse(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier =
                    Modifier
                        .width(52.dp)
                        .height(12.dp)
                        .skeletonPulse(),
            )
        }
    }
}

private const val SKELETON_COUNT = 8

@ThemePreviews
@Composable
private fun SearchSkeletonContentPreview() {
    LeafTheme {
        SearchSkeletonContent()
    }
}
