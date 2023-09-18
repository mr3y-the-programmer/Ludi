package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.ApiResult
import com.mr3y.ludi.core.network.model.MMOGamesArticle
import com.mr3y.ludi.core.network.model.MMOGiveawayEntry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

interface MMOGamesDataSource {

    suspend fun getLatestNews(url: String): ApiResult<List<MMOGamesArticle>>

    suspend fun getLatestGiveaways(url: String): ApiResult<List<MMOGiveawayEntry>>
}

class MMOGamesDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : MMOGamesDataSource {
    override suspend fun getLatestNews(url: String): ApiResult<List<MMOGamesArticle>> {
        return client.get(url).body()
    }

    override suspend fun getLatestGiveaways(url: String): ApiResult<List<MMOGiveawayEntry>> {
        return client.get(url).body()
    }
}
