package com.mr3y.ludi.core.repository.internal

import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GiveawayEntry
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.toCoreErrorResult
import com.mr3y.ludi.core.network.datasources.internal.CheapSharkDataSource
import com.mr3y.ludi.core.network.datasources.internal.GamerPowerDataSource
import com.mr3y.ludi.core.network.model.CheapSharkDeal
import com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.network.model.toDeal
import com.mr3y.ludi.core.network.model.toGiveawayEntry
import com.mr3y.ludi.core.repository.DealsRepository
import com.mr3y.ludi.core.repository.query.DealsQueryParameters
import com.mr3y.ludi.core.repository.query.GiveawaysQueryParameters
import com.mr3y.ludi.core.repository.query.buildDealsFullUrl
import com.mr3y.ludi.core.repository.query.buildGiveawaysFullUrl
import com.mr3y.ludi.core.repository.query.isValid
import com.slack.eithernet.ApiResult
import javax.inject.Inject

class DefaultDealsRepository @Inject constructor(
    private val cheapSharkDataSource: CheapSharkDataSource,
    private val gamerPowerDataSource: GamerPowerDataSource
) : DealsRepository {

    override suspend fun queryDeals(queryParameters: DealsQueryParameters): Result<List<Deal>, Throwable> {
        val fullUrl = buildDealsFullUrl(endpointUrl = "$CheapSharkBaseUrl/deals", queryParameters)
        return when (val result = cheapSharkDataSource.queryLatestDeals(fullUrl)) {
            is ApiResult.Success -> Result.Success(result.value.map(CheapSharkDeal::toDeal))
            is ApiResult.Failure -> result.toCoreErrorResult()
        }
    }

    override suspend fun queryGiveaways(queryParameters: GiveawaysQueryParameters): Result<List<GiveawayEntry>, Throwable> {
        val fullUrl = if (queryParameters.isValid()) {
            buildGiveawaysFullUrl("$GamerPowerBaseUrl/filter", queryParameters)
        } else {
            "$GamerPowerBaseUrl/giveaways"
        }
        return when (val result = gamerPowerDataSource.queryLatestGiveaways(fullUrl)) {
            is ApiResult.Success -> Result.Success(result.value.map(GamerPowerGiveawayEntry::toGiveawayEntry))
            is ApiResult.Failure -> result.toCoreErrorResult()
        }
    }

    companion object {
        private const val CheapSharkBaseUrl = "https://www.cheapshark.com/api/1.0"
        private const val GamerPowerBaseUrl = "https://www.gamerpower.com/api"
    }
}
