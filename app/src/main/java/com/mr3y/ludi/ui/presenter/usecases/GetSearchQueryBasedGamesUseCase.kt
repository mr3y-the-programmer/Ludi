package com.mr3y.ludi.ui.presenter.usecases

import com.mr3y.ludi.core.model.onSuccess
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.GamesQuery
import com.mr3y.ludi.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.ui.presenter.model.Criteria
import com.mr3y.ludi.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.Order
import com.mr3y.ludi.ui.presenter.usecases.utils.groupByGenre
import javax.inject.Inject

interface GetSearchQueryBasedGamesUseCase {
    suspend operator fun invoke(
        searchQuery: String,
        filters: DiscoverFiltersState
    ): DiscoverStateGames.SearchQueryBasedGames
}

class GetSearchQueryBasedGamesUseCaseImpl @Inject constructor(
    private val gamesRepository: GamesRepository
) : GetSearchQueryBasedGamesUseCase {

    override suspend operator fun invoke(
        searchQuery: String,
        filters: DiscoverFiltersState
    ): DiscoverStateGames.SearchQueryBasedGames {
        return gamesRepository.queryGames(
            GamesQuery(
                searchQuery = searchQuery.takeIf { it.isNotEmpty() && it.isNotBlank() },
                isFuzzinessEnabled = true,
                matchTermsExactly = false,
                platforms = filters.selectedPlatforms.map { it.id }.takeIf { it.isNotEmpty() },
                stores = filters.selectedStores.map { it.id }.takeIf { it.isNotEmpty() },
                genres = filters.selectedGenres.map { it.id }.takeIf { it.isNotEmpty() },
                sortingCriteria = run {
                    val sortingCriteria = filters.sortingCriteria ?: return@run null
                    when (sortingCriteria.criteria) {
                        Criteria.Name -> {
                            if (sortingCriteria.order == Order.Ascending) {
                                GamesSortingCriteria.NameAscending
                            } else {
                                GamesSortingCriteria.NameDescending
                            }
                        }
                        Criteria.DateReleased -> {
                            if (sortingCriteria.order == Order.Ascending) {
                                GamesSortingCriteria.ReleasedAscending
                            } else {
                                GamesSortingCriteria.ReleasedDescending
                            }
                        }
                        Criteria.DateAdded -> {
                            if (sortingCriteria.order == Order.Ascending) {
                                GamesSortingCriteria.DateAddedAscending
                            } else {
                                GamesSortingCriteria.DateAddedDescending
                            }
                        }
                        Criteria.DateCreated -> {
                            if (sortingCriteria.order == Order.Ascending) {
                                GamesSortingCriteria.DateCreatedAscending
                            } else {
                                GamesSortingCriteria.DateCreatedDescending
                            }
                        }
                        Criteria.DateUpdated -> {
                            if (sortingCriteria.order == Order.Ascending) {
                                GamesSortingCriteria.DateUpdatedAscending
                            } else {
                                GamesSortingCriteria.DateUpdatedDescending
                            }
                        }
                        Criteria.Rating -> {
                            if (sortingCriteria.order == Order.Ascending) {
                                GamesSortingCriteria.RatingAscending
                            } else {
                                GamesSortingCriteria.RatingDescending
                            }
                        }
                        Criteria.Metascore -> {
                            if (sortingCriteria.order == Order.Ascending) {
                                GamesSortingCriteria.MetacriticScoreAscending
                            } else {
                                GamesSortingCriteria.MetacriticScoreDescending
                            }
                        }
                    }
                }
            )
        ).let {
            DiscoverStateGames.SearchQueryBasedGames(
                games = it.onSuccess { gamesPage -> gamesPage.games.groupByGenre() }
            )
        }
    }
}
