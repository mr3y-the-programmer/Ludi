package com.mr3y.ludi.shared.core.repository

import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.model.Deal
import com.mr3y.ludi.shared.core.model.GiveawayEntry
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.repository.query.DealsQueryParameters
import com.mr3y.ludi.shared.core.repository.query.GiveawaysQueryParameters
import kotlinx.coroutines.flow.Flow

interface DealsRepository {

    fun queryDeals(queryParameters: DealsQueryParameters): Flow<PagingData<Deal>>

    suspend fun queryGiveaways(queryParameters: GiveawaysQueryParameters): Result<List<GiveawayEntry>, Throwable>
}
