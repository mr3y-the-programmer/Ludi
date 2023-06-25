package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.RAWGGenresPage
import com.mr3y.ludi.core.network.model.RAWGPage
import com.slack.eithernet.ApiResult
import retrofit2.http.GET
import retrofit2.http.Url

interface RAWGDataSource {

    @GET
    suspend fun queryGames(@Url url: String): ApiResult<RAWGPage, Unit>

    @GET
    suspend fun queryGamesGenres(@Url url: String): ApiResult<RAWGGenresPage, Unit>
}
