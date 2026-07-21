package io.github.jean.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.component.LeafFloatingButton
import io.github.jean.feature.home.model.HomeIntent
import io.github.jean.feature.home.model.section.HomeBookUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun HomeBookListContent(
    bookNotes: ImmutableList<HomeBookUiModel>,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(20.dp),
        ) {
            items(
                items = bookNotes,
                key = { it.noteId },
            ) { note ->
                HomeListItem(
                    model = note,
                    onIntent = onIntent,
                )
            }
        }

        LeafFloatingButton(
            modifier =
                Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 30.dp)
                    .align(Alignment.BottomEnd),
            onClick = { onIntent(HomeIntent.AddClick) },
        )
    }
}
