package io.github.jean.core.designsystem.theme

import androidx.compose.runtime.Immutable
import io.github.jean.core.designsystem.generated.resources.Res
import io.github.jean.core.designsystem.generated.resources.ic_check
import io.github.jean.core.designsystem.generated.resources.ic_chevron_down
import io.github.jean.core.designsystem.generated.resources.ic_chevron_left
import io.github.jean.core.designsystem.generated.resources.ic_chevron_right
import io.github.jean.core.designsystem.generated.resources.ic_close
import io.github.jean.core.designsystem.generated.resources.ic_leaf_error
import io.github.jean.core.designsystem.generated.resources.ic_leaf_mark
import io.github.jean.core.designsystem.generated.resources.ic_menu
import io.github.jean.core.designsystem.generated.resources.ic_moon
import io.github.jean.core.designsystem.generated.resources.ic_placeholder
import io.github.jean.core.designsystem.generated.resources.ic_plus
import io.github.jean.core.designsystem.generated.resources.ic_quote
import io.github.jean.core.designsystem.generated.resources.ic_refresh
import io.github.jean.core.designsystem.generated.resources.ic_search
import io.github.jean.core.designsystem.generated.resources.ic_sun
import io.github.jean.core.designsystem.generated.resources.ic_theme_auto
import org.jetbrains.compose.resources.DrawableResource

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
    val leafMark: DrawableResource,
    /** 뒤로가기 */
    val chevronLeft: DrawableResource,
    /** 리스트 항목 진입 */
    val chevronRight: DrawableResource,
    /** 칩 trailing — 피커 펼침 암시 */
    val chevronDown: DrawableResource,
    /** 인용 블록 레이블 */
    val quote: DrawableResource,
    /** 닫기, 인용 블록 제거 */
    val close: DrawableResource,
    /** 새 노트 FAB, 인용 추가 */
    val plus: DrawableResource,
    /** 홈 탑바 햄버거 메뉴 */
    val menu: DrawableResource,
    /** 책 검색 입력창 */
    val search: DrawableResource,
    /** 에러 화면 다시 시도 */
    val refresh: DrawableResource,
    /** 테마 밝기 — 라이트 */
    val sun: DrawableResource,
    /** 테마 밝기 — 다크 */
    val moon: DrawableResource,
    /** 테마 밝기 — 시스템 따름 */
    val themeAuto: DrawableResource,
    /** 선택 확인 표시 — 팔레트 스와치 등 */
    val check: DrawableResource,
    /** 이미지 로드 실패/부재 시 플레이스홀더 (정사각, 클립 형태 무관) */
    val placeholder: DrawableResource,
    /** 에러 화면 일러스트 — 느낌표 책갈피 */
    val error: DrawableResource,
)

val LeafResDefault =
    LeafRes(
        leafMark = Res.drawable.ic_leaf_mark,
        chevronLeft = Res.drawable.ic_chevron_left,
        chevronRight = Res.drawable.ic_chevron_right,
        chevronDown = Res.drawable.ic_chevron_down,
        quote = Res.drawable.ic_quote,
        close = Res.drawable.ic_close,
        plus = Res.drawable.ic_plus,
        menu = Res.drawable.ic_menu,
        search = Res.drawable.ic_search,
        refresh = Res.drawable.ic_refresh,
        sun = Res.drawable.ic_sun,
        moon = Res.drawable.ic_moon,
        themeAuto = Res.drawable.ic_theme_auto,
        check = Res.drawable.ic_check,
        placeholder = Res.drawable.ic_placeholder,
        error = Res.drawable.ic_leaf_error,
    )
