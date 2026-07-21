package io.github.jean.core.dataremote.util

import android.text.Html

/**
 * 알라딘 응답 텍스트는 `&lt;`, `&gt;`, `&amp;` 등으로 이스케이프되어 온다.
 * 도메인 모델로 넘기기 전에 실제 문자로 되돌린다.
 */
internal fun String.unescapeHtml(): String =
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString().trim()
