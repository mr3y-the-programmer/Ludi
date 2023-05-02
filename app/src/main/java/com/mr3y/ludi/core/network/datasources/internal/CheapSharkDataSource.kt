package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.CheapSharkDeal
import com.slack.eithernet.ApiResult
import retrofit2.http.GET
import retrofit2.http.Url

interface CheapSharkDataSource {

    @GET
    suspend fun queryLatestDeals(@Url url: String): ApiResult<List<CheapSharkDeal>, Unit>
}
