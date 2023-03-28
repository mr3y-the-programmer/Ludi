package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.MMOGamesArticle
import com.mr3y.ludi.core.network.model.MMOGiveawayEntry
import com.slack.eithernet.ApiResult
import retrofit2.http.GET
import retrofit2.http.Url

interface MMOGamesDataSource {

    @GET
    suspend fun getLatestNews(@Url url: String): ApiResult<List<MMOGamesArticle>, Unit>

    @GET
    suspend fun getLatestGiveaways(@Url url: String): ApiResult<List<MMOGiveawayEntry>, Unit>
}