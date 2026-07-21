package io.github.jean.core.ui.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.OrbitContainer
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.orbitContainer
import kotlin.time.Duration.Companion.minutes

abstract class MVIViewModel<STATE : Any, SIDE_EFFECT : Any>(
    initialState: STATE,
) : ViewModel(),
    OrbitContainerHost<STATE, STATE, SIDE_EFFECT> {
    override val container: OrbitContainer<STATE, STATE, SIDE_EFFECT> =
        orbitContainer(
            initialState = initialState,
            buildSettings = {
                exceptionHandler = coroutineExceptionHandler
                repeatOnSubscribedStopTimeout = 10.minutes.inWholeMilliseconds
            },
            onCreate = {
                onContainerCreate()
            },
        )

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception -> onError(exception) }

    protected open suspend fun Syntax<STATE, SIDE_EFFECT>.onContainerCreate() {
        // no-op
    }

    open fun onIntent(intent: Intent) = Unit

    protected open fun onError(throwable: Throwable) = Unit
}
