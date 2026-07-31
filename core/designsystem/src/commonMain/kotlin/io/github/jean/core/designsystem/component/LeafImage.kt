package io.github.jean.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import io.github.jean.core.designsystem.ThemePreviews
import io.github.jean.core.designsystem.theme.LeafTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun LeafImage(
    model: Any,
    modifier: Modifier = Modifier,
    scale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    size: Dp? = null,
    color: Color? = null,
    alpha: Float = DefaultAlpha,
    placeholder: DrawableResource? = LeafTheme.res.placeholder,
    error: DrawableResource? = LeafTheme.res.placeholder,
) {
    val sizedModifier = if (size != null) modifier.size(size) else modifier
    val colorFilter = color?.let { ColorFilter.tint(it) }

    // 로컬 드로어블은 Coil 을 거치지 않고 직접 그린다.
    // Coil 은 compose-resources 의 DrawableResource 를 모델로 이해하지 못해서
    // 넘기면 로드에 실패하고 error 폴백(placeholder)만 나온다.
    // Android 전용이던 시절에는 model 이 R.drawable 의 Int 라서 Coil 이 처리해 줬다.
    val localDrawable = model as? DrawableResource

    // 프리뷰에서는 네트워크를 못 타므로 플레이스홀더로 대체한다.
    val previewFallback = if (LocalInspectionMode.current) placeholder ?: error ?: LeafTheme.res.placeholder else null

    val localPainter = (localDrawable ?: previewFallback)?.let { painterResource(it) }
    if (localPainter != null) {
        Image(
            painter = localPainter,
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
        placeholder = placeholder?.let { painterResource(it) },
        alpha = alpha,
        contentScale = scale,
        error = error?.let { painterResource(it) },
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
