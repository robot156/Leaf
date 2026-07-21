package io.github.jean.core.designsystem.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import io.github.jean.core.designsystem.R

/**
 * Leaf 드로어블 리소스 축.
 *
 * 색·타이포처럼 리소스도 테마를 통해 꺼내 쓴다.
 * 팔레트(먹/월넛)나 다크 모드에 따라 다른 에셋으로 갈아끼울 수 있는 간접 지점.
 * 사용처: painterResource(LeafTheme.res.leafMark)
 */
@Immutable
data class LeafRes(
    /** 브랜드 마크 — 스플래시, About, 빈 화면 */
    @DrawableRes val leafMark: Int,
    /** 뒤로가기 */
    @DrawableRes val chevronLeft: Int,
    /** 리스트 항목 진입 */
    @DrawableRes val chevronRight: Int,
    /** 칩 trailing — 피커 펼침 암시 */
    @DrawableRes val chevronDown: Int,
    /** 인용 블록 레이블 */
    @DrawableRes val quote: Int,
    /** 닫기, 인용 블록 제거 */
    @DrawableRes val close: Int,
    /** 새 노트 FAB, 인용 추가 */
    @DrawableRes val plus: Int,
    /** 홈 탑바 햄버거 메뉴 */
    @DrawableRes val menu: Int,
    /** 책 검색 입력창 */
    @DrawableRes val search: Int,
    /** 에러 화면 다시 시도 */
    @DrawableRes val refresh: Int,
    /** 테마 밝기 — 라이트 */
    @DrawableRes val sun: Int,
    /** 테마 밝기 — 다크 */
    @DrawableRes val moon: Int,
    /** 테마 밝기 — 시스템 따름 */
    @DrawableRes val themeAuto: Int,
    /** 선택 확인 표시 — 팔레트 스와치 등 */
    @DrawableRes val check: Int,
    /** 이미지 로드 실패/부재 시 플레이스홀더 (정사각, 클립 형태 무관) */
    @DrawableRes val placeholder: Int,
    /** 에러 화면 일러스트 — 느낌표 책갈피 */
    @DrawableRes val error: Int,
)

val LeafResDefault =
    LeafRes(
        leafMark = R.drawable.ic_leaf_mark,
        chevronLeft = R.drawable.ic_chevron_left,
        chevronRight = R.drawable.ic_chevron_right,
        chevronDown = R.drawable.ic_chevron_down,
        quote = R.drawable.ic_quote,
        close = R.drawable.ic_close,
        plus = R.drawable.ic_plus,
        menu = R.drawable.ic_menu,
        search = R.drawable.ic_search,
        refresh = R.drawable.ic_refresh,
        sun = R.drawable.ic_sun,
        moon = R.drawable.ic_moon,
        themeAuto = R.drawable.ic_theme_auto,
        check = R.drawable.ic_check,
        placeholder = R.drawable.ic_placeholder,
        error = R.drawable.ic_leaf_error,
    )
