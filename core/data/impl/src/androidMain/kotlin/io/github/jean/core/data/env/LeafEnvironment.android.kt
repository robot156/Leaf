package io.github.jean.core.data.env

import android.os.Build

internal actual val platformOsVersion: String
    get() = Build.VERSION.RELEASE

internal actual val platformDeviceModel: String
    get() = Build.MODEL
