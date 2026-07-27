package io.github.jean.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import io.github.jean.core.designsystem.generated.resources.Res
import io.github.jean.core.designsystem.generated.resources.pretendard_regular
import io.github.jean.core.designsystem.generated.resources.pretendard_semibold
import io.github.jean.core.designsystem.generated.resources.ridi_batang
import org.jetbrains.compose.resources.Font

/**
 * 한글 폰트 상하 여백 제거 + 줄 높이 중앙 정렬 공통값.
 *
 * `includeFontPadding` 은 Android 전용 파라미터라 [koreanPlatformTextStyle] 로 분리했다.
 */
private val KoreanTextDefaults =
    TextStyle(
        platformStyle = koreanPlatformTextStyle(),
        lineHeightStyle =
            LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
    )

/**
 * Android 의 레거시 폰트 패딩을 끄기 위한 플랫폼 스타일.
 * iOS 에는 대응 개념이 없어 `null` 을 반환한다.
 */
internal expect fun koreanPlatformTextStyle(): PlatformTextStyle?

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

/**
 * compose-resources 의 `Font()` 는 `@Composable` 이라 top-level `val` 에서 호출할 수 없다.
 * (Android 의 `Font(R.font.x)` 는 가능했다) 그래서 폰트 패밀리와 타이포그래피를
 * 컴포지션 안에서 만든다.
 *
 * `LocalLeafTypography` 는 `staticCompositionLocalOf` 라 값이 바뀌면 하위 전체가
 * 재구성된다. [remember] 로 인스턴스를 고정해 [LeafTheme] 재구성 때마다 새로 만들지 않는다.
 */
@Composable
fun rememberLeafTypography(): LeafTypography {
    /**
     * 세리프(리디바탕) — 책 제목, 독서 노트 본문, 브랜드 워드마크.
     * 리디바탕은 Regular 단일 웨이트: FontWeight.Bold를 주면 faux bold로 뭉개지므로
     * 위계는 크기와 색으로만 잡는다.
     */
    val serif =
        FontFamily(
            Font(Res.font.ridi_batang, FontWeight.Normal),
        )

    /** 산세리프(프리텐다드) — UI 전반, 버튼, 메타 정보 */
    val sans =
        FontFamily(
            Font(Res.font.pretendard_regular, FontWeight.Normal),
            Font(Res.font.pretendard_semibold, FontWeight.SemiBold),
        )

    return remember(serif, sans) { leafTypography(serif = serif, sans = sans) }
}

private fun leafTypography(
    serif: FontFamily,
    sans: FontFamily,
): LeafTypography =
    LeafTypography(
        wordmark =
            KoreanTextDefaults.copy(
                fontFamily = serif,
                fontSize = 30.sp,
                lineHeight = 38.sp,
                letterSpacing = 0.5.sp,
            ),
        wordmarkSmall =
            KoreanTextDefaults.copy(
                fontFamily = serif,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.5.sp,
            ),
        tagline =
            KoreanTextDefaults.copy(
                fontFamily = sans,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 4.sp,
            ),
        bookTitle =
            KoreanTextDefaults.copy(
                fontFamily = serif,
                fontSize = 22.sp,
                lineHeight = 33.sp,
            ),
        bookTitleSmall =
            KoreanTextDefaults.copy(
                fontFamily = serif,
                fontSize = 18.sp,
                lineHeight = 26.sp,
            ),
        note =
            KoreanTextDefaults.copy(
                fontFamily = serif,
                fontSize = 15.sp,
                lineHeight = 27.sp,
            ),
        quote =
            KoreanTextDefaults.copy(
                fontFamily = serif,
                fontSize = 16.sp,
                lineHeight = 28.sp,
            ),
        label =
            KoreanTextDefaults.copy(
                fontFamily = sans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        body =
            KoreanTextDefaults.copy(
                fontFamily = sans,
                fontSize = 15.sp,
                lineHeight = 22.sp,
            ),
        meta =
            KoreanTextDefaults.copy(
                fontFamily = sans,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
    )
