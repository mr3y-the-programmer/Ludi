package com.mr3y.ludi.core.repository

import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.RichInfoGamesPage
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.repository.query.FreeGamesQueryParameters
import com.mr3y.ludi.core.repository.query.RichInfoGamesQueryParameters
import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    fun queryFreeGames(queryParameters: FreeGamesQueryParameters): Flow<Result<List<FreeGame>, Throwable>>

    fun queryRichInfoGames(queryParameters: RichInfoGamesQueryParameters): Flow<Result<RichInfoGamesPage, Throwable>>
}