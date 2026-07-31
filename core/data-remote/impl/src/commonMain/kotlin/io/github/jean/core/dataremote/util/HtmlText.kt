package io.github.jean.core.dataremote.util

/**
 * 알라딘 응답 텍스트는 `&lt;`, `&gt;`, `&amp;` 등으로 이스케이프되어 온다.
 * 도메인 모델로 넘기기 전에 실제 문자로 되돌린다.
 *
 * 기존 Android 구현(`Html.fromHtml`)을 순수 Kotlin 으로 대체한 것이다.
 * 처리 순서가 중요하다 — 태그 제거를 **먼저** 하고 엔티티를 나중에 푼다.
 * 순서를 뒤집으면 `&lt;b&gt;` 가 실제 태그로 변한 뒤 제거되어, 본문에 있던
 * 꺾쇠 텍스트가 사라진다. (`Html.fromHtml` 도 입력을 한 번만 파싱하므로 동작이 같다)
 */
internal fun String.unescapeHtml(): String =
    replace(LINE_BREAK_TAG, "\n")
        .replace(ANY_TAG, "")
        .decodeHtmlEntities()
        .trim()

/** `<br>`, `</p>`, `</div>` 는 줄바꿈으로 살린다. */
private val LINE_BREAK_TAG = Regex("""<\s*(br|/p|/div)\s*/?\s*>""", RegexOption.IGNORE_CASE)

private val ANY_TAG = Regex("""<[^>]*>""")

/** `&name;`, `&#123;`, `&#x1F34E;` 세 형태를 받는다. */
private val ENTITY = Regex("""&(#[xX][0-9a-fA-F]+|#[0-9]+|[a-zA-Z][a-zA-Z0-9]*);""")

private val NAMED_ENTITIES =
    mapOf(
        "lt" to "<",
        "gt" to ">",
        "amp" to "&",
        "quot" to "\"",
        "apos" to "'",
        "nbsp" to " ",
        "middot" to "·",
        "hellip" to "…",
        "mdash" to "—",
        "ndash" to "–",
        "lsquo" to "‘",
        "rsquo" to "’",
        "ldquo" to "“",
        "rdquo" to "”",
    )

private fun String.decodeHtmlEntities(): String =
    ENTITY.replace(this) { match ->
        val body = match.groupValues[1]
        val decoded =
            when {
                body.startsWith("#x") || body.startsWith("#X") ->
                    body.drop(2).toIntOrNull(radix = 16)?.codePointToString()

                body.startsWith("#") -> body.drop(1).toIntOrNull()?.codePointToString()
                else -> NAMED_ENTITIES[body]
            }
        // 모르는 엔티티는 원문 그대로 남긴다. 임의로 지우면 본문이 유실된다.
        decoded ?: match.value
    }

/**
 * BMP 를 넘는 코드포인트는 서로게이트 페어로 직접 조립한다.
 * (JVM 의 `Character.toChars` 는 공용 코드에서 쓸 수 없다)
 */
private fun Int.codePointToString(): String =
    when (this) {
        in 0..0xFFFF -> toChar().toString()
        in 0x10000..0x10FFFF -> toSurrogatePair()
        else -> ""
    }

private fun Int.toSurrogatePair(): String {
    val offset = this - 0x10000
    return charArrayOf(
        (0xD800 + (offset shr 10)).toChar(),
        (0xDC00 + (offset and 0x3FF)).toChar(),
    ).concatToString()
}
