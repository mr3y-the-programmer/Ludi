package com.mr3y.ludi.shared.core.repository

import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.model.GamesGenresPage
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.repository.query.GamesQueryParameters
import kotlinx.coroutines.flow.Flow

interface GamesRepository {
    fun queryGames(queryParameters: GamesQueryParameters): Flow<PagingData<Game>>

    suspend fun queryGamesGenres(): Result<GamesGenresPage, Throwable>
}
