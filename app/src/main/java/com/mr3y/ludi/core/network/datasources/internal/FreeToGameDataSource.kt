package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.FreeToGameGame
import com.slack.eithernet.ApiResult
import retrofit2.http.GET
import retrofit2.http.Url

interface FreeToGameDataSource {

    @GET
    suspend fun queryGames(@Url url: String): ApiResult<List<FreeToGameGame>, Unit>
}