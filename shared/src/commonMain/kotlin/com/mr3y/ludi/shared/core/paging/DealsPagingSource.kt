package com.mr3y.ludi.shared.core.paging

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import com.mr3y.ludi.shared.core.CrashReporting
import com.mr3y.ludi.shared.core.model.Deal
import com.mr3y.ludi.shared.core.network.datasources.internal.CheapSharkDataSource
import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.toDeal
import com.mr3y.ludi.shared.core.repository.query.DealsQueryParameters

class DealsPagingSource(
    private val networkDataSource: CheapSharkDataSource,
    private val query: DealsQueryParameters,
    private val crashReporting: CrashReporting
) : PagingSource<Int, Deal>() {

    override fun getRefreshKey(state: PagingState<Int, Deal>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Deal> {
        val pageNum = params.key?.coerceAtLeast(0) ?: 0
        return when (val response = networkDataSource.queryLatestDeals(query, pageNum, params.loadSize)) {
            is ApiResult.Success -> {
                LoadResult.Page(
                    data = response.data.deals.map { it.toDeal() },
                    prevKey = if (pageNum > 0) pageNum - 1 else null,
                    nextKey = if (pageNum < response.data.totalPageCount) pageNum + 1 else null
                )
            }
            is ApiResult.Error -> {
                if (response.throwable != null) {
                    crashReporting.recordException(response.throwable, logMessage = "Error occurred while querying deals with query $query")
                    return LoadResult.Error(response.throwable)
                }
                return LoadResult.Invalid()
            }
        }
    }
}
