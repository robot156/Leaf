package io.github.jean.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.theme.LeafTheme

@Composable
fun LeafImage(
    model: Any,
    modifier: Modifier = Modifier,
    scale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    size: Dp? = null,
    color: Color? = null,
    alpha: Float = DefaultAlpha,
    @DrawableRes placeholder: Int? = LeafTheme.res.placeholder,
    @DrawableRes error: Int? = LeafTheme.res.placeholder,
) {
    val sizedModifier = if (size != null) modifier.size(size) else modifier
    val colorFilter = color?.let { ColorFilter.tint(it) }

    if (LocalInspectionMode.current) {
        val painterResource =
            when (model) {
                is Int -> {
                    painterResource(model)
                }

                else -> {
                    (placeholder ?: error)
                        ?.let { painterResource(it) }
                        ?: painterResource(LeafTheme.res.placeholder)
                }
            }

        Image(
            painter = painterResource,
            contentDescription = contentDescription,
            modifier = sizedModifier,
            alpha = alpha,
            colorFilter = colorFilter,
            contentScale = scale,
        )

        return
    }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = sizedModifier,
        placeholder = placeholder?.let { painterResource(id = it) },
        alpha = alpha,
        contentScale = scale,
        error = error?.let { painterResource(id = it) },
        colorFilter = colorFilter,
    )
}

@ThemePreviews
@Composable
private fun LeafImagePreview() {
    LeafTheme {
        LeafImage(model = LeafTheme.res.placeholder)
    }
}
