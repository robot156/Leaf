package io.github.jean.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.jean.core.common.model.LeafPalette

/**
 * Leaf 색상 role.
 *
 * 팔레트(잉크 네이비, 포레스트, 버건디, 먹, 올리브, 테라코타) × 밝기(라이트/다크)
 * 조합마다 이 role 세트가 하나씩 정의된다. 다크는 hue 유지 원칙 —
 * 무채색 어둠이 아니라 각 팔레트의 기운이 남아 있는 어둠.
 */
@Immutable
data class LeafColors(
    /** 잉크색 — 브랜드, 주요 액션, 선택 상태 */
    val primary: Color,
    /** primary 위에 올라가는 콘텐츠 */
    val onPrimary: Color,
    /** 잉크의 중간톤 — 인용 캡션, 보조 강조 */
    val primaryMuted: Color,
    /** 화면 바탕 — 종이 */
    val surface: Color,
    /** 배경보다 반 톤 가라앉은 면 — 보더 없는 톤온톤 카드 (라이선스 목록 등) */
    val surfaceDim: Color,
    /** 카드, 입력 필드 바탕 */
    val card: Color,
    /** 카드 테두리, 구분선 */
    val border: Color,
    /** 스켈레톤 로딩 박스 */
    val skeleton: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val isDark: Boolean,
)

// ── 잉크 네이비 ── 만년필 잉크. 밤의 독서 (기본)

val InkNavyLight =
    LeafColors(
        primary = Color(0xFF1F2B45),
        onPrimary = Color(0xFFF7F3E9),
        primaryMuted = Color(0xFF44506E),
        surface = Color(0xFFF5F4EF),
        surfaceDim = Color(0xFFEFEDE5),
        card = Color(0xFFFEFDFA),
        border = Color(0xFFE3E1D8),
        skeleton = Color(0xFFE0DFDC),
        textPrimary = Color(0xFF22201C),
        textSecondary = Color(0xFF55534C),
        textMuted = Color(0xFF8B8A82),
        isDark = false,
    )

val InkNavyDark =
    LeafColors(
        primary = Color(0xFFA9B7D8),
        onPrimary = Color(0xFF15181F),
        primaryMuted = Color(0xFF8493B8),
        surface = Color(0xFF15181F),
        surfaceDim = Color(0xFF191D25),
        card = Color(0xFF1D212B),
        border = Color(0xFF2C3140),
        skeleton = Color(0xFF272B34),
        textPrimary = Color(0xFFE9E7E0),
        textSecondary = Color(0xFFB5B3AA),
        textMuted = Color(0xFF807F77),
        isDark = true,
    )

// ── 포레스트 ── 잎 본연의 색

val ForestLight =
    LeafColors(
        primary = Color(0xFF1E4034),
        onPrimary = Color(0xFFF1F5EC),
        primaryMuted = Color(0xFF43604F),
        surface = Color(0xFFF4F6F1),
        surfaceDim = Color(0xFFEDF0E8),
        card = Color(0xFFFDFEFC),
        border = Color(0xFFDFE4DA),
        skeleton = Color(0xFFDCE0D9),
        textPrimary = Color(0xFF1F231E),
        textSecondary = Color(0xFF51564E),
        textMuted = Color(0xFF878C83),
        isDark = false,
    )

val ForestDark =
    LeafColors(
        primary = Color(0xFFA3C4B1),
        onPrimary = Color(0xFF131B17),
        primaryMuted = Color(0xFF6E8A7B),
        surface = Color(0xFF131B17),
        surfaceDim = Color(0xFF18211C),
        card = Color(0xFF1A2420),
        border = Color(0xFF28372F),
        skeleton = Color(0xFF233029),
        textPrimary = Color(0xFFE5EAE4),
        textSecondary = Color(0xFFAEB6AD),
        textMuted = Color(0xFF79817A),
        isDark = true,
    )

// ── 버건디 ── 가죽 장정 고전

val BurgundyLight =
    LeafColors(
        primary = Color(0xFF5E2431),
        onPrimary = Color(0xFFF9F0EA),
        primaryMuted = Color(0xFF7E4A55),
        surface = Color(0xFFF8F3EE),
        surfaceDim = Color(0xFFF1EAE2),
        card = Color(0xFFFEFBF8),
        border = Color(0xFFE8DDD5),
        skeleton = Color(0xFFE4DAD3),
        textPrimary = Color(0xFF251E1C),
        textSecondary = Color(0xFF5A4F4C),
        textMuted = Color(0xFF8F8481),
        isDark = false,
    )

val BurgundyDark =
    LeafColors(
        primary = Color(0xFFD0A0AB),
        onPrimary = Color(0xFF1D1416),
        primaryMuted = Color(0xFF9A7079),
        surface = Color(0xFF1D1416),
        surfaceDim = Color(0xFF231A1C),
        card = Color(0xFF271B1E),
        border = Color(0xFF372A2E),
        skeleton = Color(0xFF2E2226),
        textPrimary = Color(0xFFECE5E3),
        textSecondary = Color(0xFFB9AFAC),
        textMuted = Color(0xFF837878),
        isDark = true,
    )

// ── 먹 ── 활자와 먹

val MukLight =
    LeafColors(
        primary = Color(0xFF26241F),
        onPrimary = Color(0xFFFAF7F0),
        primaryMuted = Color(0xFF55524A),
        surface = Color(0xFFFAF9F5),
        surfaceDim = Color(0xFFF2F1EA),
        card = Color(0xFFFFFEFB),
        border = Color(0xFFE6E4DC),
        skeleton = Color(0xFFE2E0D9),
        textPrimary = Color(0xFF22201C),
        textSecondary = Color(0xFF55534C),
        textMuted = Color(0xFF8B8A82),
        isDark = false,
    )

val MukDark =
    LeafColors(
        primary = Color(0xFFC9C5BB),
        onPrimary = Color(0xFF191815),
        primaryMuted = Color(0xFF8F8B81),
        surface = Color(0xFF191815),
        surfaceDim = Color(0xFF1F1E1A),
        card = Color(0xFF22211D),
        border = Color(0xFF32302B),
        skeleton = Color(0xFF2A2925),
        textPrimary = Color(0xFFE9E7E0),
        textSecondary = Color(0xFFB5B3AA),
        textMuted = Color(0xFF807F77),
        isDark = true,
    )

// ── 올리브 ── 빛바랜 식물도감

val OliveLight =
    LeafColors(
        primary = Color(0xFF575A3A),
        onPrimary = Color(0xFFF6F6EA),
        primaryMuted = Color(0xFF76795A),
        surface = Color(0xFFF6F5EC),
        surfaceDim = Color(0xFFEFEEE1),
        card = Color(0xFFFDFCF6),
        border = Color(0xFFE2E1D2),
        skeleton = Color(0xFFDEDDD0),
        textPrimary = Color(0xFF232419),
        textSecondary = Color(0xFF555648),
        textMuted = Color(0xFF8B8C7D),
        isDark = false,
    )

val OliveDark =
    LeafColors(
        primary = Color(0xFFBFC29A),
        onPrimary = Color(0xFF191A12),
        primaryMuted = Color(0xFF8A8D6C),
        surface = Color(0xFF191A12),
        surfaceDim = Color(0xFF1F2017),
        card = Color(0xFF22231A),
        border = Color(0xFF33342A),
        skeleton = Color(0xFF2A2B22),
        textPrimary = Color(0xFFE8E8DF),
        textSecondary = Color(0xFFB4B4A6),
        textMuted = Color(0xFF7F8073),
        isDark = true,
    )

// ── 테라코타 ── 흙과 낙엽

val TerracottaLight =
    LeafColors(
        primary = Color(0xFFA8502F),
        onPrimary = Color(0xFFFBF3EC),
        primaryMuted = Color(0xFF9C5E41),
        surface = Color(0xFFFBF4ED),
        surfaceDim = Color(0xFFF4EBE1),
        card = Color(0xFFFFFBF6),
        border = Color(0xFFEBDFD3),
        skeleton = Color(0xFFE7DCD2),
        textPrimary = Color(0xFF27201B),
        textSecondary = Color(0xFF5C524B),
        textMuted = Color(0xFF928680),
        isDark = false,
    )

val TerracottaDark =
    LeafColors(
        primary = Color(0xFFE0A488),
        onPrimary = Color(0xFF1E1512),
        primaryMuted = Color(0xFFA9765C),
        surface = Color(0xFF1E1512),
        surfaceDim = Color(0xFF251B17),
        card = Color(0xFF281D18),
        border = Color(0xFF3A2C24),
        skeleton = Color(0xFF30241E),
        textPrimary = Color(0xFFEDE6E1),
        textSecondary = Color(0xFFBBB0A9),
        textMuted = Color(0xFF857A73),
        isDark = true,
    )

/**
 * 팔레트 × 밝기 → 색 세트.
 * LeafTheme이 이 함수 하나로 팔레트를 해석한다.
 */
fun LeafPalette.colors(isDarkTheme: Boolean): LeafColors =
    when (this) {
        LeafPalette.InkNavy -> if (isDarkTheme) InkNavyDark else InkNavyLight
        LeafPalette.Forest -> if (isDarkTheme) ForestDark else ForestLight
        LeafPalette.Burgundy -> if (isDarkTheme) BurgundyDark else BurgundyLight
        LeafPalette.Muk -> if (isDarkTheme) MukDark else MukLight
        LeafPalette.Olive -> if (isDarkTheme) OliveDark else OliveLight
        LeafPalette.Terracotta -> if (isDarkTheme) TerracottaDark else TerracottaLight
    }
