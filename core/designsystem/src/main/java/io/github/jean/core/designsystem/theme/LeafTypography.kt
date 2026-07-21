package io.github.jean.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import io.github.jean.core.designsystem.R

/**
 * 세리프(리디바탕) — 책 제목, 독서 노트 본문, 브랜드 워드마크.
 * 리디바탕은 Regular 단일 웨이트: FontWeight.Bold를 주면 faux bold로 뭉개지므로
 * 위계는 크기와 색으로만 잡는다.
 */
val LeafSerif =
    FontFamily(
        Font(R.font.ridi_batang, FontWeight.Normal),
    )

/** 산세리프(프리텐다드) — UI 전반, 버튼, 메타 정보 */
val LeafSans =
    FontFamily(
        Font(R.font.pretendard_regular, FontWeight.Normal),
        Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    )

/** 한글 폰트 상하 여백 제거 + 줄 높이 중앙 정렬 공통값 */
private val KoreanTextDefaults =
    TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle =
            LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
    )

@Immutable
data class LeafTypography(
    /** 브랜드 워드마크 — 세리프 30sp. 스플래시, 온보딩 */
    val wordmark: TextStyle,
    /** 탑바용 워드마크 — 세리프 22sp. 홈 등 루트 화면 탑바 */
    val wordmarkSmall: TextStyle,
    /** 브랜드 태그라인 — 산스 12sp, 넓은 자간. 워드마크와 짝으로 사용 */
    val tagline: TextStyle,
    /** 상세 화면 책 제목 — 세리프 22sp */
    val bookTitle: TextStyle,
    /** 리스트 카드 책 제목 — 세리프 18sp */
    val bookTitleSmall: TextStyle,
    /** 독서 노트 본문 — 세리프 15sp, 넉넉한 행간 */
    val note: TextStyle,
    /** 인용문 — 세리프 16sp (한글은 이탤릭 대신 크기·색으로 구분) */
    val quote: TextStyle,
    /** UI 레이블, 버튼 — 산스 14sp */
    val label: TextStyle,
    /** UI 본문 — 산스 15sp */
    val body: TextStyle,
    /** 메타 정보 (날짜, 노트 수) — 산스 12sp */
    val meta: TextStyle,
)

val LeafTypographyDefault =
    LeafTypography(
        wordmark =
            KoreanTextDefaults.copy(
                fontFamily = LeafSerif,
                fontSize = 30.sp,
                lineHeight = 38.sp,
                letterSpacing = 0.5.sp,
            ),
        wordmarkSmall =
            KoreanTextDefaults.copy(
                fontFamily = LeafSerif,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.5.sp,
            ),
        tagline =
            KoreanTextDefaults.copy(
                fontFamily = LeafSans,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 4.sp,
            ),
        bookTitle =
            KoreanTextDefaults.copy(
                fontFamily = LeafSerif,
                fontSize = 22.sp,
                lineHeight = 33.sp,
            ),
        bookTitleSmall =
            KoreanTextDefaults.copy(
                fontFamily = LeafSerif,
                fontSize = 18.sp,
                lineHeight = 26.sp,
            ),
        note =
            KoreanTextDefaults.copy(
                fontFamily = LeafSerif,
                fontSize = 15.sp,
                lineHeight = 27.sp,
            ),
        quote =
            KoreanTextDefaults.copy(
                fontFamily = LeafSerif,
                fontSize = 16.sp,
                lineHeight = 28.sp,
            ),
        label =
            KoreanTextDefaults.copy(
                fontFamily = LeafSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        body =
            KoreanTextDefaults.copy(
                fontFamily = LeafSans,
                fontSize = 15.sp,
                lineHeight = 22.sp,
            ),
        meta =
            KoreanTextDefaults.copy(
                fontFamily = LeafSans,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
    )
