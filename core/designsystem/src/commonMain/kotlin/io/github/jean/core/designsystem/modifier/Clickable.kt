package io.github.jean.core.designsystem.modifier

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.delay

private const val DEFAULT_INTERVAL_DURATION = 500L

@Composable
fun Modifier.throttleClickable(
    enabled: Boolean = true,
    duration: Long = DEFAULT_INTERVAL_DURATION,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = ripple(),
    role: Role? = null,
    onClick: () -> Unit,
): Modifier {
    var clickAvailable by remember { mutableStateOf(true) }

    LaunchedEffect(clickAvailable) {
        if (clickAvailable) {
            return@LaunchedEffect
        }

        delay(timeMillis = duration)
        clickAvailable = true
    }

    return this.clickable(
        enabled = enabled && clickAvailable,
        indication = indication,
        interactionSource = interactionSource,
        role = role,
    ) {
        if (!clickAvailable) {
            return@clickable
        }

        clickAvailable = false
        onClick()
    }
}
