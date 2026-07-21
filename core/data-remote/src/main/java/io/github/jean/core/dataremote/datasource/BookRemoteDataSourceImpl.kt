package io.github.jean.core.dataremote.datasource

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.jean.core.common.env.Environment
import io.github.jean.core.dataremote.model.BookContainerResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class BookRemoteDataSourceImpl(
    private val client: HttpClient,
    private val environment: Environment,
) : BookRemoteDataSource {
    override suspend fun getSearchBooks(
        query: String,
        page: Int,
        pageSize: Int,
    ): BookContainerResponse =
        client
            .get(environment.baseApiUrl) {
                parameter("ttbkey", environment.apiKey)
                parameter("Query", query)
                parameter("QueryType", "Title")
                parameter("SearchTarget", "Book")
                parameter("MaxResults", pageSize)
                parameter("start", page)
                parameter("output", "js")
                parameter("Version", API_VERSION)
            }.body()

    override suspend fun getBook(itemId: Long): BookContainerResponse {
        val lookUpUrl = environment.baseApiUrl.substringBeforeLast('/') + "/" + ITEM_LOOK_UP_PATH
        return client
            .get(lookUpUrl) {
                parameter("ttbkey", environment.apiKey)
                parameter("ItemIdType", "ItemId")
                parameter("ItemId", itemId)
                parameter("output", "js")
                parameter("Version", API_VERSION)
            }.body()
    }

    companion object {
        private const val API_VERSION = "20131101"
        private const val ITEM_LOOK_UP_PATH = "ItemLookUp.aspx"
    }
}
