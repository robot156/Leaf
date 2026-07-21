package io.github.jean.feature.write.editor.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.component.LeafNoteTextField
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun EditorRecordBlockField(
    state: TextFieldState,
    showPlaceholder: Boolean,
    requestFocus: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(requestFocus) {
        if (requestFocus) focusRequester.requestFocus()
    }

    LeafNoteTextField(
        state = state,
        placeholder = if (showPlaceholder) stringResource(R.string.editor_record_placeholder) else null,
        modifier =
            modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
    )
}

@ThemePreviews
@Composable
private fun EditorRecordBlockFieldPreview() {
    LeafTheme {
        EditorRecordBlockField(
            state = rememberTextFieldState("여럿으로 사는 일이 그에게는 불안이 아니라 존재 방식이었다"),
            showPlaceholder = false,
            requestFocus = true,
        )
    }
}
