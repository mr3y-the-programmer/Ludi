package com.mr3y.ludi.shared.core.paging

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.mr3y.ludi.shared.core.CrashReporting
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.toGame
import com.mr3y.ludi.shared.core.repository.query.GamesQueryParameters

class RAWGGamesPagingSource(
    private val networkDataSource: RAWGDataSource,
    private val query: GamesQueryParameters,
    private val crashReporting: CrashReporting
) : PagingSource<Int, Game>() {

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val pageNum = params.key?.coerceAtLeast(1) ?: 1
        return when (val response = networkDataSource.queryGames(query, pageNum, params.loadSize)) {
            is ApiResult.Success -> {
                LoadResult.Page(
                    data = response.data.results.mapNotNull { it.toGame() },
                    prevKey = if (response.data.previousPageUrl != null) pageNum - 1 else null,
                    nextKey = if (response.data.nextPageUrl != null) pageNum + 1 else null
                )
            }
            is ApiResult.Error -> {
                if (response.throwable != null) {
                    crashReporting.recordException(response.throwable, logMessage = "Error occurred while querying RAWG Games with query $query")
                    return LoadResult.Error(response.throwable)
                }
                return LoadResult.Invalid()
            }
        }
    }
}
