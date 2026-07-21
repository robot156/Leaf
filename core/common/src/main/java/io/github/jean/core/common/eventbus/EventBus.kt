package io.github.jean.core.common.eventbus

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@SingleIn(AppScope::class)
@Inject
class EventBus<T : EventAction> {
    private val _action =
        MutableSharedFlow<T>(
            extraBufferCapacity = Int.MAX_VALUE,
            onBufferOverflow = BufferOverflow.DROP_LATEST,
        )
    val action: SharedFlow<T> = _action

    fun send(action: T) = _action.tryEmit(action)
}
