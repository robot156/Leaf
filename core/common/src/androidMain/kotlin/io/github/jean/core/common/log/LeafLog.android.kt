package io.github.jean.core.common.log

import android.util.Log

actual fun logDebug(
    tag: String,
    message: String,
) {
    Log.d(tag, message)
}
