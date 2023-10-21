package com.mr3y.ludi.shared.core.network.datasources.internal

import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.shared.core.repository.query.GiveawaysQueryParameters
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import me.tatarka.inject.annotations.Inject

interface GamerPowerDataSource {

    suspend fun queryLatestGiveaways(queryParameters: GiveawaysQueryParameters): ApiResult<List<GamerPowerGiveawayEntry>>
}

@Inject
class GamerPowerDataSourceImpl(
    private val client: HttpClient
) : GamerPowerDataSource {
    override suspend fun queryLatestGiveaways(queryParameters: GiveawaysQueryParameters): ApiResult<List<GamerPowerGiveawayEntry>> {
        return try {
            val endpointUrl = if (queryParameters.platforms != null) "$GamerPowerBaseUrl/filter" else "$GamerPowerBaseUrl/giveaways"
            val response = client.get(endpointUrl) {
                if (queryParameters.platforms != null) {
                    parameter("platform", queryParameters.platforms.joinToString(separator = ".") { it.value })
                    parameter("sort-by", queryParameters.sorting?.value)
                }
            }
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(code = response.status.value)
            }
        } catch (e: Exception) {
            ApiResult.Error(code = null, throwable = e)
        }
    }

    companion object {
        private const val GamerPowerBaseUrl = "https://www.gamerpower.com/api"
    }
}
