package com.mr3y.ludi.core.repository

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGamesGenresPage
import com.mr3y.ludi.core.model.RichInfoGamesPage
import com.mr3y.ludi.core.repository.query.RichInfoGamesQueryParameters

interface GamesRepository {
    suspend fun queryRichInfoGames(queryParameters: RichInfoGamesQueryParameters): Result<RichInfoGamesPage, Throwable>

    suspend fun queryGamesGenres(): Result<RichInfoGamesGenresPage, Throwable>
}
