package io.github.jean.leaf.di

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

/**
 * Hilt가 자동으로 해주던 @ApplicationContext 바인딩을 명시적으로 선언.
 * DI에서 타입은 정확히 일치해야 하므로 Application → Context 별칭이 필요.
 */
@ContributesTo(AppScope::class)
@BindingContainer
interface AndroidBindings {
    @Binds
    fun bindContext(application: Application): Context
}
