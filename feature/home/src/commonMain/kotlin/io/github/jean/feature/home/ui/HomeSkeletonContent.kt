package io.github.jean.feature.home.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.skeletonPulse
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun HomeSkeletonContent(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(SKELETON_COUNT) {
            HomeListItemSkeleton()
        }
    }
}

@Composable
private fun HomeListItemSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(12.dp)
                .background(LeafTheme.colors.card),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .skeletonPulse(0.dp),
        )

        Column(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 14.dp, bottom = 12.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.7f)
                        .height(18.dp)
                        .skeletonPulse(),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier =
                        Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .skeletonPulse(),
                )

                Box(
                    modifier =
                        Modifier
                            .width(64.dp)
                            .height(12.dp)
                            .skeletonPulse(),
                )
            }
        }
    }
}

private const val SKELETON_COUNT = 5
