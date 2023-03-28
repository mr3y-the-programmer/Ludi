package com.mr3y.ludi.core.repository

import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.model.MMOGiveawayEntry
import com.mr3y.ludi.core.model.Result
import kotlinx.coroutines.flow.Flow

interface DealsRepository {

    fun queryDeals(): Flow<Result<List<Deal>, Throwable>>

    fun queryMMOGiveaways(): Flow<Result<List<MMOGiveawayEntry>, Throwable>>

    fun queryGamerPowerGiveaways(): Flow<Result<List<GamerPowerGiveawayEntry>, Throwable>>
}