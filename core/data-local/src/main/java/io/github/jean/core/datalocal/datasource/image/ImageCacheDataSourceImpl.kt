package io.github.jean.core.datalocal.datasource.image

import android.content.Context
import coil3.SingletonImageLoader
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class ImageCacheDataSourceImpl(
    private val context: Context,
) : ImageCacheDataSource {
    override suspend fun clear() {
        val imageLoader = SingletonImageLoader.get(context)
        imageLoader.memoryCache?.clear()
        // 디스크 캐시 삭제는 파일 IO라 IO 디스패처에서 수행한다.
        withContext(Dispatchers.IO) {
            imageLoader.diskCache?.clear()
        }
    }
}
