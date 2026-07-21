package io.github.jean.core.data.env

import android.os.Build
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.env.Environment
import io.github.jean.core.data.BuildConfig

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class AndroidEnvironment : Environment {
    override val isDebug: Boolean
        get() = BuildConfig.DEBUG
    override val baseApiUrl: String
        get() = BuildConfig.API_URL
    override val apiKey: String
        get() = BuildConfig.API_KEY
    override val appVersion: String
        get() = BuildConfig.VERSION_NAME
    override val osVersion: String
        get() = Build.VERSION.RELEASE
    override val deviceModel: String
        get() = Build.MODEL
}
