package io.github.jean.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import io.github.jean.core.common.model.LeafPalette

val LocalLeafColors =
    staticCompositionLocalOf<LeafColors> {
        error("LeafAppTheme 바깥에서 LeafColors에 접근했습니다")
    }

val LocalLeafTypography =
    staticCompositionLocalOf<LeafTypography> {
        error("LeafAppTheme 바깥에서 LeafTypography에 접근했습니다")
    }

val LocalLeafRes =
    staticCompositionLocalOf<LeafRes> {
        error("LeafAppTheme 바깥에서 LeafRes에 접근했습니다")
    }
val LocalLeafPalette =
    staticCompositionLocalOf<LeafPalette> {
        error("LeafAppTheme 바깥에서 LocalLeafPalette에 접근했습니다")
    }
val LocalIsDarkTheme = staticCompositionLocalOf { false }

/**
 * 사용처:
 * LeafTheme.colors.primary
 * LeafTheme.typography.note
 * painterResource(LeafTheme.res.leafMark)
 */
object LeafTheme {
    val colors: LeafColors
        @Composable
        @ReadOnlyComposable
        get() = LocalLeafColors.current
    val typography: LeafTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalLeafTypography.current
    val res: LeafRes
        @Composable
        @ReadOnlyComposable
        get() = LocalLeafRes.current
    val isDarkTheme: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalIsDarkTheme.current
    val palette: LeafPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalLeafPalette.current
}

@Composable
fun LeafTheme(
    palette: LeafPalette = LeafPalette.InkNavy,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = palette.colors(isDarkTheme)

    CompositionLocalProvider(
        LocalLeafColors provides colors,
        LocalLeafTypography provides LeafTypographyDefault,
        LocalLeafRes provides LeafResDefault,
        LocalDensity provides Density(density = LocalDensity.current.density, fontScale = 1f),
        LocalIsDarkTheme provides isDarkTheme,
        LocalLeafPalette provides palette,
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialScheme(),
            content = content,
        )
    }
}

/**
 * M3 컴포넌트(TextField, Switch, Snackbar 등)가 팔레트를 따라오도록 매핑.
 * 커스텀 컴포넌트는 LeafTheme.colors를 직접 읽는다.
 */
private fun LeafColors.toMaterialScheme(): ColorScheme {
    val base = if (isDark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = primary,
        onPrimary = onPrimary,
        secondary = primaryMuted,
        onSecondary = onPrimary,
        background = surface,
        onBackground = textPrimary,
        surface = card,
        onSurface = textPrimary,
        surfaceVariant = surface,
        onSurfaceVariant = textSecondary,
        outline = border,
        outlineVariant = border,
    )
}
