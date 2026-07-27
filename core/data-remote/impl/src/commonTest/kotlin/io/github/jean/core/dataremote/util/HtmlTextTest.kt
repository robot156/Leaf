package io.github.jean.core.dataremote.util

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * `unescapeHtml` 은 CMP 전환 과정에서 `Html.fromHtml` 을 순수 Kotlin 으로 다시 쓴 것이다.
 * 책 제목·소개글이 조용히 깨지는 걸 막기 위한 회귀 테스트.
 */
class HtmlTextTest {
    @Test
    fun `이스케이프된 꺾쇠는 실제 문자로 복원된다`() {
        assertEquals("<b>", "&lt;b&gt;".unescapeHtml())
    }

    @Test
    fun `앰퍼샌드와 따옴표를 복원한다`() {
        assertEquals("""Tom & Jerry "quoted" 'apos'""", "Tom &amp; Jerry &quot;quoted&quot; &apos;apos&apos;".unescapeHtml())
    }

    @Test
    fun `실제 태그는 제거한다`() {
        assertEquals("굵게", "<b>굵게</b>".unescapeHtml())
    }

    @Test
    fun `br 태그는 줄바꿈이 된다`() {
        assertEquals("첫줄\n둘째줄", "첫줄<br>둘째줄".unescapeHtml())
        assertEquals("첫줄\n둘째줄", "첫줄<br />둘째줄".unescapeHtml())
    }

    @Test
    fun `이스케이프된 태그는 태그로 해석되지 않는다`() {
        // 엔티티를 먼저 풀면 <br> 로 변해 줄바꿈이 되어버린다. 순서가 뒤집히지 않았는지 확인한다.
        assertEquals("<br>", "&lt;br&gt;".unescapeHtml())
    }

    @Test
    fun `10진수와 16진수 숫자 참조를 복원한다`() {
        assertEquals("A", "&#65;".unescapeHtml())
        assertEquals("A", "&#x41;".unescapeHtml())
    }

    @Test
    fun `BMP 를 넘는 코드포인트는 서로게이트 페어로 조립한다`() {
        assertEquals("🍎", "&#x1F34E;".unescapeHtml())
    }

    @Test
    fun `모르는 엔티티는 원문을 유지한다`() {
        assertEquals("&unknownentity;", "&unknownentity;".unescapeHtml())
    }

    @Test
    fun `앞뒤 공백을 제거한다`() {
        assertEquals("본문", "  본문  ".unescapeHtml())
    }
}
