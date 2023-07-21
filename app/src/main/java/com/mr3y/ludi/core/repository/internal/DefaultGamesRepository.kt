package com.mr3y.ludi.core.repository.internal

import com.mr3y.ludi.core.model.GamesGenresPage
import com.mr3y.ludi.core.model.GamesPage
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.toCoreErrorResult
import com.mr3y.ludi.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.core.network.model.toGamesGenresPage
import com.mr3y.ludi.core.network.model.toGamesPage
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.GamesQueryParameters
import com.mr3y.ludi.core.repository.query.buildGamesFullUrl
import com.slack.eithernet.ApiResult
import javax.inject.Inject

class DefaultGamesRepository @Inject constructor(
    private val rawgDataSource: RAWGDataSource
) : GamesRepository {

    override suspend fun queryGames(queryParameters: GamesQueryParameters): Result<GamesPage, Throwable> {
        val fullUrl = buildGamesFullUrl(endpointUrl = "$RAWGApiBaseUrl/games", queryParameters)
        return when (val result = rawgDataSource.queryGames(fullUrl)) {
            is ApiResult.Success -> {
                Result.Success(result.value.toGamesPage())
            }
            is ApiResult.Failure -> {
                result.toCoreErrorResult()
            }
        }
    }

    override suspend fun queryGamesGenres(): Result<GamesGenresPage, Throwable> {
        return when (val result = rawgDataSource.queryGamesGenres("$RAWGApiBaseUrl/genres?ordering=name")) {
            is ApiResult.Success -> {
                Result.Success(result.value.toGamesGenresPage())
            }
            is ApiResult.Failure -> {
                result.toCoreErrorResult()
            }
        }
    }

    companion object {
        private const val RAWGApiBaseUrl = "https://api.rawg.io/api"
    }
}
