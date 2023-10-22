package com.mr3y.ludi.shared.core.repository.internal

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.CrashReporting
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.model.GamesGenresPage
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.toCoreErrorResult
import com.mr3y.ludi.shared.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.toGamesGenresPage
import com.mr3y.ludi.shared.core.paging.RAWGGamesPagingSource
import com.mr3y.ludi.shared.core.repository.GamesRepository
import com.mr3y.ludi.shared.core.repository.query.GamesQueryParameters
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class DefaultGamesRepository(
    private val rawgDataSource: RAWGDataSource,
    private val crashReporting: CrashReporting
) : GamesRepository {

    override fun queryGames(queryParameters: GamesQueryParameters): Flow<PagingData<Game>> {
        return Pager(DefaultPagingConfig) {
            RAWGGamesPagingSource(
                rawgDataSource,
                queryParameters,
                crashReporting
            )
        }.flow
    }

    override suspend fun queryGamesGenres(): Result<GamesGenresPage, Throwable> {
        return when (val result = rawgDataSource.queryGamesGenres()) {
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
        private val DefaultPagingConfig = PagingConfig(pageSize = 20, initialLoadSize = 20)
    }
}
