package com.mr3y.ludi.shared.core.repository.internal

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.CrashReporting
import com.mr3y.ludi.shared.core.model.Deal
import com.mr3y.ludi.shared.core.model.GiveawayEntry
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.toCoreErrorResult
import com.mr3y.ludi.shared.core.network.datasources.internal.CheapSharkDataSource
import com.mr3y.ludi.shared.core.network.datasources.internal.GamerPowerDataSource
import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.shared.core.network.model.toGiveawayEntry
import com.mr3y.ludi.shared.core.paging.DealsPagingSource
import com.mr3y.ludi.shared.core.repository.DealsRepository
import com.mr3y.ludi.shared.core.repository.query.DealsQueryParameters
import com.mr3y.ludi.shared.core.repository.query.GiveawaysQueryParameters
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class DefaultDealsRepository(
    private val cheapSharkDataSource: CheapSharkDataSource,
    private val gamerPowerDataSource: GamerPowerDataSource,
    private val crashReporting: CrashReporting
) : DealsRepository {

    override fun queryDeals(queryParameters: DealsQueryParameters): Flow<PagingData<Deal>> {
        return Pager(DefaultPagingConfig) {
            DealsPagingSource(
                cheapSharkDataSource,
                queryParameters,
                crashReporting
            )
        }.flow
    }

    override suspend fun queryGiveaways(queryParameters: GiveawaysQueryParameters): Result<List<GiveawayEntry>, Throwable> {
        return when (val result = gamerPowerDataSource.queryLatestGiveaways(queryParameters)) {
            is ApiResult.Success -> Result.Success(result.data.map(GamerPowerGiveawayEntry::toGiveawayEntry))
            is ApiResult.Error -> result.toCoreErrorResult().also { reportExceptions(it, "Error occurred while querying giveaways with query $queryParameters") }
        }
    }

    private fun reportExceptions(result: Result.Error, message: String?) {
        if (result.exception != null) {
            crashReporting.recordException(result.exception, message)
        }
    }

    companion object {
        private val DefaultPagingConfig = PagingConfig(pageSize = 20, initialLoadSize = 20)
    }
}
