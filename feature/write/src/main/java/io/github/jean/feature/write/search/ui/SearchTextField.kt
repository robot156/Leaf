package io.github.jean.feature.write.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafBoxTextField
import io.github.jean.core.designsystem.component.LeafTextButton
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun SearchTextField(
    state: TextFieldState,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasText by remember { derivedStateOf { state.text.isNotEmpty() } }

    Row(
        modifier =
            modifier
                .padding(top = 10.dp)
                .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LeafBoxTextField(
            modifier = Modifier.weight(1f),
            state = state,
            leadingIcon = LeafTheme.res.search,
            placeholder = stringResource(R.string.search_placeholder),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            onKeyboardAction = { onSearch() },
        )

        AnimatedVisibility(
            visible = hasText,
            enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
            exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
        ) {
            LeafTextButton(
                modifier = Modifier.padding(start = 4.dp),
                text = stringResource(R.string.search_action),
                onClick = onSearch,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun SearchTextFieldEmptyPreview() {
    LeafTheme {
        SearchTextField(
            state = rememberTextFieldState(),
            onSearch = {},
        )
    }
}

@ThemePreviews
@Composable
private fun SearchTextFieldFilledPreview() {
    LeafTheme {
        SearchTextField(
            state = rememberTextFieldState(initialText = "페소아"),
            onSearch = {},
        )
    }
}
