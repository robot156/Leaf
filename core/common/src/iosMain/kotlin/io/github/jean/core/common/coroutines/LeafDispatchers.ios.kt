package io.github.jean.core.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Kotlin/Native 에서 `Dispatchers.IO` 는 아직 internal 이라 참조할 수 없다.
 * Darwin 타깃에서는 `Dispatchers.Default` 가 멀티스레드 워커 풀이므로 이것으로 대체한다.
 * (파일·DB 접근이 짧아 풀을 오래 점유하지 않는다)
 */
actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.Default
