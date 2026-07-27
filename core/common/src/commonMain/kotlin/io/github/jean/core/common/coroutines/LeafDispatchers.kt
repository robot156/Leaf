package io.github.jean.core.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * 파일·DB 등 블로킹 IO 용 디스패처.
 *
 * `Dispatchers.IO` 는 kotlinx-coroutines 가 `concurrent`(jvm + native) 소스셋에만 선언해서
 * commonMain 에서는 참조할 수 없다. 플랫폼별 actual 로 연결한다.
 */
expect val ioDispatcher: CoroutineDispatcher
