package com.mr3y.ludi.core.repository.fixtures

import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGamesPage
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.FreeGamesQueryParameters
import com.mr3y.ludi.core.repository.query.RichInfoGamesQueryParameters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeGamesRepository : GamesRepository {

//    private val richInfoGamesResult =

    fun addMockedResponse() {
    }

    override suspend fun queryFreeGames(queryParameters: FreeGamesQueryParameters): Result<List<FreeGame>, Throwable> {
        TODO("Not yet implemented")
    }

    override suspend fun queryRichInfoGames(queryParameters: RichInfoGamesQueryParameters): Result<RichInfoGamesPage, Throwable> {
        TODO("Not yet implemented")
    }
}
