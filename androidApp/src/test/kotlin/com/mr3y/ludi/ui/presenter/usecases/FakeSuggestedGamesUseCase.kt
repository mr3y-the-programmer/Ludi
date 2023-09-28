package com.mr3y.ludi.ui.presenter.usecases

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.GamesQuery
import com.mr3y.ludi.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.TaggedGames
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FakeSuggestedGamesUseCase(private val gamesRepository: GamesRepository) : GetSuggestedGamesUseCase {

    private var cache: DiscoverStateGames.SuggestedGames? = null

    override suspend fun invoke(page: Int): DiscoverStateGames.SuggestedGames {
        return if (cache != null) {
            cache!!
        } else {
            val gamesResult = gamesRepository.queryGames(
                GamesQuery(
                    pageSize = 20,
                    dates = listOf(
                        LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                    ),
                    metaCriticScores = listOf(85, 100),
                    sortingCriteria = GamesSortingCriteria.DateUpdatedDescending
                )
            )
            val games = when (gamesResult) {
                is Result.Loading -> gamesResult
                is Result.Success -> Result.Success(gamesResult.data.games)
                is Result.Error -> gamesResult
            }
            DiscoverStateGames.SuggestedGames(
                listOf(TaggedGames.TrendingGames(games))
            ).also { cache = it }
        }
    }
}
