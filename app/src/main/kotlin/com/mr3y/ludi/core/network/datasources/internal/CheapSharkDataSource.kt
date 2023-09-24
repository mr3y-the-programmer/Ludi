package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.ApiResult
import com.mr3y.ludi.core.network.model.CheapSharkDeal
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import me.tatarka.inject.annotations.Inject

interface CheapSharkDataSource {

    suspend fun queryLatestDeals(url: String): ApiResult<List<CheapSharkDeal>>
}

@Inject
class CheapSharkDataSourceImpl(
    private val client: HttpClient
) : CheapSharkDataSource {
    override suspend fun queryLatestDeals(url: String): ApiResult<List<CheapSharkDeal>> {
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
