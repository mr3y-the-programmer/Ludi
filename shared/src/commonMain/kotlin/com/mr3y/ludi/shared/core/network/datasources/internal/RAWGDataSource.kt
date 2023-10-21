package com.mr3y.ludi.shared.core.network.datasources.internal

import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.RAWGGenresPage
import com.mr3y.ludi.shared.core.network.model.RAWGPage
import com.mr3y.ludi.shared.core.repository.query.GamesQueryParameters
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import me.tatarka.inject.annotations.Inject

interface RAWGDataSource {

    suspend fun queryGames(queryParameters: GamesQueryParameters, page: Int = 1, pageSize: Int = 50): ApiResult<RAWGPage>

    suspend fun queryGamesGenres(): ApiResult<RAWGGenresPage>
}

@Inject
class RAWGDataSourceImpl(
    private val client: HttpClient
) : RAWGDataSource {
    override suspend fun queryGames(queryParameters: GamesQueryParameters, page: Int, pageSize: Int): ApiResult<RAWGPage> {
        return try {
            val response = client.get("$RAWGApiBaseUrl/games") {
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("search", queryParameters.searchQuery)
                parameter("search_precise", queryParameters.isFuzzinessEnabled)
                parameter("search_exact", queryParameters.matchTermsExactly)
                parameter("parent_platforms", queryParameters.parentPlatforms?.joinToString(separator = ","))
                parameter("platforms", queryParameters.platforms?.joinToString(separator = ","))
                parameter("stores", queryParameters.stores?.joinToString(separator = ","))
                parameter("developers", queryParameters.developers?.joinToString(separator = ","))
                parameter("genres", queryParameters.genres?.joinToString(separator = ","))
                parameter("tags", queryParameters.tags?.joinToString(separator = ","))
                parameter("dates", queryParameters.dates?.joinToString(separator = ","))
                parameter("metacritic", queryParameters.metaCriticScores?.joinToString(separator = ","))
                parameter("ordering", queryParameters.sortingCriteria?.apiName)
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

    override suspend fun queryGamesGenres(): ApiResult<RAWGGenresPage> {
        return try {
            val response = client.get("$RAWGApiBaseUrl/genres?ordering=name")
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
        private const val RAWGApiBaseUrl = "https://api.rawg.io/api"
    }
}
