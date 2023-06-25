package com.mr3y.ludi.core.repository.internal

import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGamesGenresPage
import com.mr3y.ludi.core.model.RichInfoGamesPage
import com.mr3y.ludi.core.model.toCoreErrorResult
import com.mr3y.ludi.core.network.datasources.internal.FreeToGameDataSource
import com.mr3y.ludi.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.core.network.model.FreeToGameGame
import com.mr3y.ludi.core.network.model.toFreeGame
import com.mr3y.ludi.core.network.model.toRichInfoGamesGenresPage
import com.mr3y.ludi.core.network.model.toRichInfoGamesPage
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.FreeGamesQueryParameters
import com.mr3y.ludi.core.repository.query.RichInfoGamesQueryParameters
import com.mr3y.ludi.core.repository.query.buildFreeGamesFullUrl
import com.mr3y.ludi.core.repository.query.buildRichInfoGamesFullUrl
import com.slack.eithernet.ApiResult
import javax.inject.Inject

class DefaultGamesRepository @Inject constructor(
    private val freeToGameDataSource: FreeToGameDataSource,
    private val rawgDataSource: RAWGDataSource
) : GamesRepository {

    override suspend fun queryFreeGames(queryParameters: FreeGamesQueryParameters): Result<List<FreeGame>, Throwable> {
        val fullUrl = buildFreeGamesFullUrl(endpointUrl = "$FreeToGameBaseUrl/games", queryParameters)
        return when (val result = freeToGameDataSource.queryGames(fullUrl)) {
            is ApiResult.Success -> {
                Result.Success(result.value.map(FreeToGameGame::toFreeGame))
            }
            is ApiResult.Failure -> {
                result.toCoreErrorResult()
            }
        }
    }

    override suspend fun queryRichInfoGames(queryParameters: RichInfoGamesQueryParameters): Result<RichInfoGamesPage, Throwable> {
        val fullUrl = buildRichInfoGamesFullUrl(endpointUrl = "$RAWGApiBaseUrl/games", queryParameters)
        return when (val result = rawgDataSource.queryGames(fullUrl)) {
            is ApiResult.Success -> {
                Result.Success(result.value.toRichInfoGamesPage())
            }
            is ApiResult.Failure -> {
                result.toCoreErrorResult()
            }
        }
    }

    override suspend fun queryGamesGenres(): Result<RichInfoGamesGenresPage, Throwable> {
        return when (val result = rawgDataSource.queryGamesGenres("$RAWGApiBaseUrl/genres?ordering=name")) {
            is ApiResult.Success -> {
                Result.Success(result.value.toRichInfoGamesGenresPage())
            }
            is ApiResult.Failure -> {
                result.toCoreErrorResult()
            }
        }
    }

    companion object {
        private const val FreeToGameBaseUrl = "https://www.freetogame.com/api"
        private const val RAWGApiBaseUrl = "https://api.rawg.io/api"
    }
}
