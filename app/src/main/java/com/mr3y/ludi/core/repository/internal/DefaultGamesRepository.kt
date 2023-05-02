package com.mr3y.ludi.core.repository.internal

import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGamesPage
import com.mr3y.ludi.core.model.toCoreErrorResult
import com.mr3y.ludi.core.network.datasources.internal.FreeToGameDataSource
import com.mr3y.ludi.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.core.network.model.FreeToGameGame
import com.mr3y.ludi.core.network.model.toFreeGame
import com.mr3y.ludi.core.network.model.toRichInfoGamesPage
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.FreeGamesQueryParameters
import com.mr3y.ludi.core.repository.query.RichInfoGamesQueryParameters
import com.mr3y.ludi.core.repository.query.buildFreeGamesFullUrl
import com.mr3y.ludi.core.repository.query.buildRichInfoGamesFullUrl
import com.slack.eithernet.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultGamesRepository @Inject constructor(
    private val freeToGameDataSource: FreeToGameDataSource,
    private val rawgDataSource: RAWGDataSource
) : GamesRepository {

    override fun queryFreeGames(queryParameters: FreeGamesQueryParameters): Flow<Result<List<FreeGame>, Throwable>> = flow {
        val fullUrl = buildFreeGamesFullUrl(endpointUrl = "$FreeToGameBaseUrl/games", queryParameters)
        when (val result = freeToGameDataSource.queryGames(fullUrl)) {
            is ApiResult.Success -> {
                emit(Result.Success(result.value.map(FreeToGameGame::toFreeGame)))
            }
            is ApiResult.Failure -> {
                emit(result.toCoreErrorResult())
            }
        }
    }

    override fun queryRichInfoGames(queryParameters: RichInfoGamesQueryParameters): Flow<Result<RichInfoGamesPage, Throwable>> = flow {
        val fullUrl = buildRichInfoGamesFullUrl(endpointUrl = "$RAWGApiBaseUrl/games", queryParameters)
        when (val result = rawgDataSource.queryGames(fullUrl)) {
            is ApiResult.Success -> {
                emit(Result.Success(result.value.toRichInfoGamesPage()))
            }
            is ApiResult.Failure -> {
                emit(result.toCoreErrorResult())
            }
        }
    }

    companion object {
        private const val FreeToGameBaseUrl = "https://www.freetogame.com/api"
        private const val RAWGApiBaseUrl = "https://api.rawg.io/api"
    }
}
