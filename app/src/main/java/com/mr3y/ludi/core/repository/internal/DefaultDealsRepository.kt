package com.mr3y.ludi.core.repository.internal

import com.mr3y.ludi.core.model.*
import com.mr3y.ludi.core.network.datasources.internal.CheapSharkDataSource
import com.mr3y.ludi.core.network.datasources.internal.GamerPowerDataSource
import com.mr3y.ludi.core.network.datasources.internal.MMOGamesDataSource
import com.mr3y.ludi.core.network.model.CheapSharkDeal
import com.mr3y.ludi.core.network.model.toCoreGamerPowerGiveawayEntry
import com.mr3y.ludi.core.network.model.toCoreGiveawayEntry
import com.mr3y.ludi.core.network.model.toDeal
import com.mr3y.ludi.core.repository.DealsRepository
import com.mr3y.ludi.core.repository.query.DealsQueryParameters
import com.mr3y.ludi.core.repository.query.GiveawaysQueryParameters
import com.mr3y.ludi.core.repository.query.buildDealsFullUrl
import com.mr3y.ludi.core.repository.query.buildGiveawaysFullUrl
import com.mr3y.ludi.core.repository.query.isValid
import com.slack.eithernet.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultDealsRepository @Inject constructor(
    private val cheapSharkDataSource: CheapSharkDataSource,
    private val gamerPowerDataSource: GamerPowerDataSource,
    private val mmoGamesDataSource: MMOGamesDataSource
) : DealsRepository {

    override fun queryDeals(queryParameters: DealsQueryParameters): Flow<Result<List<Deal>, Throwable>> = flow {
        val fullUrl = buildDealsFullUrl(endpointUrl = "$CheapSharkBaseUrl/deals", queryParameters)
        when(val result = cheapSharkDataSource.queryLatestDeals(fullUrl)) {
            is ApiResult.Success -> emit(Result.Success(result.value.map(CheapSharkDeal::toDeal)))
            is ApiResult.Failure -> emit(result.toCoreErrorResult())
        }
    }

    override fun queryMMOGiveaways(): Flow<Result<List<MMOGiveawayEntry>, Throwable>> = flow {
        when(val result = mmoGamesDataSource.getLatestGiveaways("$MMOGamesBaseUrl/giveaways")) {
            is ApiResult.Success -> emit(Result.Success(result.value.map(com.mr3y.ludi.core.network.model.MMOGiveawayEntry::toCoreGiveawayEntry)))
            is ApiResult.Failure -> emit(result.toCoreErrorResult())
        }
    }

    override fun queryGamerPowerGiveaways(queryParameters: GiveawaysQueryParameters): Flow<Result<List<GamerPowerGiveawayEntry>, Throwable>> = flow {
        val fullUrl = if (queryParameters.isValid())
            buildGiveawaysFullUrl("$GamerPowerBaseUrl/filter", queryParameters)
        else
            "$GamerPowerBaseUrl/giveaways"
        when(val result = gamerPowerDataSource.queryLatestGiveaways(fullUrl)) {
            is ApiResult.Success -> emit(Result.Success(result.value.map(com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntry::toCoreGamerPowerGiveawayEntry)))
            is ApiResult.Failure -> emit(result.toCoreErrorResult())
        }
    }

    companion object {
        private const val MMOGamesBaseUrl = "https://www.mmobomb.com/api1"
        private const val CheapSharkBaseUrl = "https://www.cheapshark.com/api/1.0"
        private const val GamerPowerBaseUrl = "https://www.gamerpower.com/api"
    }
}