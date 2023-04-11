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

    override fun queryFreeGames(queryParameters: FreeGamesQueryParameters): Flow<Result<List<FreeGame>, Throwable>> {
        return emptyFlow()
    }

    override fun queryRichInfoGames(queryParameters: RichInfoGamesQueryParameters): Flow<Result<RichInfoGamesPage, Throwable>> {
        TODO("Not yet implemented")
    }
}