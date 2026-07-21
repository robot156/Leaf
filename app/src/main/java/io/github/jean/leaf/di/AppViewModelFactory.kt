package io.github.jean.leaf.di

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import kotlin.reflect.KClass

/**
 * 앱 전체에서 딱 하나 필요한 고정 배선 코드 (Hilt는 이걸 내부에서 숨겨서 생성).
 * 로직 없음 — 맵 3개를 받아 넘기는 게 전부. 다시 열어볼 일 없는 파일.
 */
@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Suppress("ktlint:standard:parameter-wrapping") // ktlint 직접 사용 시
class AppViewModelFactory(
    override val viewModelProviders: Map<KClass<out ViewModel>, () -> ViewModel>,
    override val assistedFactoryProviders: Map<KClass<out ViewModel>, () -> ViewModelAssistedFactory>,
    override val manualAssistedFactoryProviders:
    Map<KClass<out ManualViewModelAssistedFactory>, () -> ManualViewModelAssistedFactory>,
) : MetroViewModelFactory()
