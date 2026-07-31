package io.github.jean.core.datalocal.datasource.image

import coil3.PlatformContext
import coil3.SingletonImageLoader
import io.github.jean.core.common.coroutines.ioDispatcher
import kotlinx.coroutines.withContext

/**
 * [PlatformContext] 는 Coil 의 멀티플랫폼 컨텍스트다.
 * Android 에서는 `android.content.Context` 의 typealias, iOS 에서는 싱글턴이다.
 */
internal class ImageCacheDataSourceImpl(
    private val context: PlatformContext,
) : ImageCacheDataSource {
    override suspend fun clear() {
        val imageLoader = SingletonImageLoader.get(context)
        imageLoader.memoryCache?.clear()
        // 디스크 캐시 삭제는 파일 IO라 IO 디스패처에서 수행한다.
        withContext(ioDispatcher) {
            imageLoader.diskCache?.clear()
        }
    }
}
