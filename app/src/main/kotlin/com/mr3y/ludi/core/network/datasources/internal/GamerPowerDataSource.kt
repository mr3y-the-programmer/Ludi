package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.ApiResult
import com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javax.inject.Inject

interface GamerPowerDataSource {

    suspend fun queryLatestGiveaways(url: String): ApiResult<List<GamerPowerGiveawayEntry>>
}

class GamerPowerDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : GamerPowerDataSource {
    override suspend fun queryLatestGiveaways(url: String): ApiResult<List<GamerPowerGiveawayEntry>> {
        return try {
            val response = client.get(url)
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(code = response.status.value)
            }
        } catch (e: Exception) {
            ApiResult.Error(code = null, throwable = e)
        }
    }
}
