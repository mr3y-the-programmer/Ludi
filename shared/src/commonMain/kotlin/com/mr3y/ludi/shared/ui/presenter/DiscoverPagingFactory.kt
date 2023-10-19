package com.mr3y.ludi.shared.ui.presenter

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import com.mr3y.ludi.shared.LudiSharedState
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.shared.core.paging.RAWGGamesPagingSource
import com.mr3y.ludi.shared.core.repository.query.GamesQuery
import com.mr3y.ludi.shared.core.repository.query.GamesSortingCriteria
import com.mr3y.ludi.shared.ui.presenter.model.Criteria
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.shared.ui.presenter.model.Order
import me.tatarka.inject.annotations.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface DiscoverPagingFactory {

    val trendingGamesPager: Pager<Int, Game>

    val topRatedGamesPager: Pager<Int, Game>

    val favGenresBasedGamesPager: Pager<Int, Game>?

    val multiplayerGamesPager: Pager<Int, Game>

    val freeGamesPager: Pager<Int, Game>

    val storyGamesPager: Pager<Int, Game>

    val boardGamesPager: Pager<Int, Game>

    val esportsGamesPager: Pager<Int, Game>

    val raceGamesPager: Pager<Int, Game>

    val puzzleGamesPager: Pager<Int, Game>

    val soundtrackGamesPager: Pager<Int, Game>

    fun createPagerForGamesWithQuery(searchQuery: String, filters: DiscoverFiltersState): Pager<Int, Game>
}

@Inject
class DefaultDiscoverPagingFactory(
    private val networkDataSource: RAWGDataSource,
    private val appState: LudiSharedState
) : DiscoverPagingFactory {

    override val trendingGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(
                dates = listOf(
                    LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE),
                    LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                ),
                metaCriticScores = listOf(85, 100),
                sortingCriteria = GamesSortingCriteria.DateUpdatedDescending
            )
        )
    }

    override val topRatedGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(metaCriticScores = listOf(95, 100))
        )
    }

    override val favGenresBasedGamesPager: Pager<Int, Game>? = appState.userFavouriteGenresIds.takeIf { !it.isNullOrEmpty() }?.let {
        Pager(DefaultPagingConfig) {
            RAWGGamesPagingSource(
                networkDataSource,
                GamesQuery(genres = it)
            )
        }
    }

    override val multiplayerGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(genres = listOf(59))
        )
    }

    override val freeGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(tags = listOf(79))
        )
    }

    override val storyGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(tags = listOf(406))
        )
    }

    override val boardGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(tags = listOf(162))
        )
    }

    override val esportsGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(tags = listOf(73))
        )
    }

    override val raceGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(tags = listOf(1407))
        )
    }

    override val puzzleGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(tags = listOf(83, 1867))
        )
    }

    override val soundtrackGamesPager: Pager<Int, Game> = Pager(DefaultPagingConfig) {
        RAWGGamesPagingSource(
            networkDataSource,
            GamesQuery(tags = listOf(42))
        )
    }

    override fun createPagerForGamesWithQuery(
        searchQuery: String,
        filters: DiscoverFiltersState
    ): Pager<Int, Game> {
        return Pager(DefaultPagingConfig) {
            RAWGGamesPagingSource(
                networkDataSource,
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

    companion object {
        val DefaultPagingConfig = PagingConfig(pageSize = 20, initialLoadSize = 20)
    }
}
