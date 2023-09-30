package com.mr3y.ludi.shared.core.repository.internal

import com.mr3y.ludi.shared.core.CrashReporting
import com.mr3y.ludi.shared.core.model.GamesGenresPage
import com.mr3y.ludi.shared.core.model.GamesPage
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.toCoreErrorResult
import com.mr3y.ludi.shared.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.toGamesGenresPage
import com.mr3y.ludi.shared.core.network.model.toGamesPage
import com.mr3y.ludi.shared.core.repository.GamesRepository
import com.mr3y.ludi.shared.core.repository.query.GamesQueryParameters
import com.mr3y.ludi.shared.core.repository.query.buildGamesFullUrl
import me.tatarka.inject.annotations.Inject

@Inject
class DefaultGamesRepository(
    private val rawgDataSource: RAWGDataSource,
    private val crashReporting: CrashReporting
) : GamesRepository {

    override suspend fun queryGames(queryParameters: GamesQueryParameters): Result<GamesPage, Throwable> {
        val fullUrl = buildGamesFullUrl(endpointUrl = "$RAWGApiBaseUrl/games", queryParameters)
        return when (val result = rawgDataSource.queryGames(fullUrl)) {
            is ApiResult.Success -> {
                Result.Success(result.data.toGamesPage())
            }
            is ApiResult.Error -> {
                val errorResult = result.toCoreErrorResult()
                if (errorResult.exception != null) {
                    crashReporting.recordException(errorResult.exception, logMessage = "Error occurred while querying RAWG Games with query $queryParameters")
                }
                errorResult
            }
        }
    }

    override suspend fun queryGamesGenres(): Result<GamesGenresPage, Throwable> {
        return when (val result = rawgDataSource.queryGamesGenres("$RAWGApiBaseUrl/genres?ordering=name")) {
            is ApiResult.Success -> {
                Result.Success(result.data.toGamesGenresPage())
            }
            is ApiResult.Error -> {
                val errorResult = result.toCoreErrorResult()
                if (errorResult.exception != null) {
                    crashReporting.recordException(errorResult.exception, logMessage = "Error occurred while querying RAWG game Genres")
                }
                errorResult
            }
        }
    }

    companion object {
        private const val RAWGApiBaseUrl = "https://api.rawg.io/api"
    }
}
