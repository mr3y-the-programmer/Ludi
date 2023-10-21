package com.mr3y.ludi.shared.core.network.datasources.internal

import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.CheapSharkResponse
import com.mr3y.ludi.shared.core.repository.query.DealsQueryParameters
import com.mr3y.ludi.shared.core.repository.query.DealsSortingDirection
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import me.tatarka.inject.annotations.Inject

interface CheapSharkDataSource {

    suspend fun queryLatestDeals(queryParameters: DealsQueryParameters, page: Int = 0, pageSize: Int = 60): ApiResult<CheapSharkResponse>
}

@Inject
class CheapSharkDataSourceImpl(
    private val client: HttpClient
) : CheapSharkDataSource {
    override suspend fun queryLatestDeals(queryParameters: DealsQueryParameters, page: Int, pageSize: Int): ApiResult<CheapSharkResponse> {
        return try {
            val response = client.get("https://www.cheapshark.com/api/1.0/deals") {
                parameter("pageNumber", page)
                parameter("pageSize", pageSize)
                parameter("title", queryParameters.searchQuery)
                parameter("exact", queryParameters.matchTermsExactly?.let { if (it) 1 else 0 })
                parameter("storeID", queryParameters.stores?.joinToString(separator = ","))
                parameter("sortBy", queryParameters.sorting?.value)
                parameter("desc", queryParameters.sortingDirection?.let { if (it == DealsSortingDirection.Descending) 1 else 0 })
            }
            if (response.status.isSuccess()) {
                ApiResult.Success(
                    CheapSharkResponse(totalPageCount = response.headers["x-total-page-count"]!!.toInt(), deals = response.body())
                )
            } else {
                ApiResult.Error(code = response.status.value)
            }
        } catch (e: Exception) {
            ApiResult.Error(code = null, throwable = e)
        }
    }
}
