package com.mr3y.ludi.shared.ui.presenter

import app.cash.paging.PagingData
import com.mr3y.ludi.shared.LudiSharedState
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.repository.GamesRepository
import com.mr3y.ludi.shared.core.repository.query.GamesQuery
import com.mr3y.ludi.shared.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.shared.ui.presenter.model.Criteria
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.shared.ui.presenter.model.Order
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface DiscoverPagingFactory {

    val trendingGamesPager: Flow<PagingData<Game>>

    val topRatedGamesPager: Flow<PagingData<Game>>

    val favGenresBasedGamesPager: Flow<PagingData<Game>>?

    val multiplayerGamesPager: Flow<PagingData<Game>>

    val freeGamesPager: Flow<PagingData<Game>>

    val storyGamesPager: Flow<PagingData<Game>>

    val boardGamesPager: Flow<PagingData<Game>>

    val esportsGamesPager: Flow<PagingData<Game>>

    val raceGamesPager: Flow<PagingData<Game>>

    val puzzleGamesPager: Flow<PagingData<Game>>

    val soundtrackGamesPager: Flow<PagingData<Game>>

    fun searchQueryBasedGamesPager(searchQuery: String, filters: DiscoverFiltersState): Flow<PagingData<Game>>
}

@Inject
class DefaultDiscoverPagingFactory(
    private val gamesRepository: GamesRepository,
    private val appState: LudiSharedState
) : DiscoverPagingFactory {

    override val trendingGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(
        GamesQuery(
            dates = listOf(
                LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE),
                LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            ),
            metaCriticScores = listOf(85, 100),
            sortingCriteria = GamesSortingCriteria.DateUpdatedDescending
        )
    )

    override val topRatedGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(metaCriticScores = listOf(95, 100)))

    override val favGenresBasedGamesPager: Flow<PagingData<Game>>? = appState.userFavouriteGenresIds.takeIf { !it.isNullOrEmpty() }?.let {
        gamesRepository.queryGames(GamesQuery(genres = it))
    }

    override val multiplayerGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(genres = listOf(59)))

    override val freeGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(tags = listOf(79)))

    override val storyGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(tags = listOf(406)))

    override val boardGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(tags = listOf(162)))

    override val esportsGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(tags = listOf(73)))

    override val raceGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(tags = listOf(1407)))

    override val puzzleGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(tags = listOf(83, 1867)))

    override val soundtrackGamesPager: Flow<PagingData<Game>> = gamesRepository.queryGames(GamesQuery(tags = listOf(42)))

    override fun searchQueryBasedGamesPager(
        searchQuery: String,
        filters: DiscoverFiltersState
    ): Flow<PagingData<Game>> {
        return gamesRepository.queryGames(
            GamesQuery(
                searchQuery = searchQuery.takeIf { it.isNotEmpty() && it.isNotBlank() },
                isFuzzinessEnabled = true,
                matchTermsExactly = false,
                platforms = filters.selectedPlatforms.map { it.id }.takeIf { it.isNotEmpty() },
                stores = filters.selectedStores.map { it.id }.takeIf { it.isNotEmpty() },
                tags = filters.selectedTags.map { it.id }.takeIf { it.isNotEmpty() },
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
        )
    }
}
