package com.mr3y.ludi.core.repository

import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.repository.query.DealsQueryParameters
import com.mr3y.ludi.core.repository.query.GiveawaysQueryParameters

interface DealsRepository {

    suspend fun queryDeals(queryParameters: DealsQueryParameters): Result<List<Deal>, Throwable>

    suspend fun queryGamerPowerGiveaways(queryParameters: GiveawaysQueryParameters): Result<List<GamerPowerGiveawayEntry>, Throwable>
}
