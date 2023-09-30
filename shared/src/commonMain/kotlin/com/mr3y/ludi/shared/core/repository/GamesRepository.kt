package com.mr3y.ludi.shared.core.repository

import com.mr3y.ludi.shared.core.model.GamesGenresPage
import com.mr3y.ludi.shared.core.model.GamesPage
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.repository.query.GamesQueryParameters

interface GamesRepository {
    suspend fun queryGames(queryParameters: GamesQueryParameters): Result<GamesPage, Throwable>

    suspend fun queryGamesGenres(): Result<GamesGenresPage, Throwable>
}
