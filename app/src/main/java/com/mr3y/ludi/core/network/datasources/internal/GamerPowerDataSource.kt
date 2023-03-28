package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntry
import com.slack.eithernet.ApiResult
import retrofit2.http.GET
import retrofit2.http.Url

interface GamerPowerDataSource {

    @GET
    suspend fun queryLatestGiveaways(@Url url: String): ApiResult<List<GamerPowerGiveawayEntry>, Unit>
}