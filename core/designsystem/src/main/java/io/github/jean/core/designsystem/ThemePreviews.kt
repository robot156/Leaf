package io.github.jean.core.designsystem

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "1. Light",
    uiMode = UI_MODE_NIGHT_NO,
    backgroundColor = 0xFFF5F4EF,
    showBackground = true,
)
@Preview(
    name = "2. Dark",
    uiMode = UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF15181F,
    showBackground = true,
)
annotation class ThemePreviews
