package com.mr3y.ludi.ui.presenter

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.core.model.RichInfoGamesPage
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.FreeGamesPlatform
import com.mr3y.ludi.core.repository.query.FreeGamesQueryParameters
import com.mr3y.ludi.core.repository.query.FreeGamesSortingCriteria
import com.mr3y.ludi.core.repository.query.RichInfoGamesQuery
import com.mr3y.ludi.core.repository.query.RichInfoGamesSortingCriteria
import com.mr3y.ludi.ui.presenter.model.Criteria
import com.mr3y.ludi.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.ui.presenter.model.DiscoverState
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.Genre
import com.mr3y.ludi.ui.presenter.model.Order
import com.mr3y.ludi.ui.presenter.model.Platform
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.Store
import com.mr3y.ludi.ui.presenter.model.wrapResource
import com.mr3y.ludi.ui.presenter.model.wrapResultResources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    private var searchQuery = mutableStateOf("")

    private val _filterState = MutableStateFlow(InitialFiltersState)

    private val games = combine(
        snapshotFlow { searchQuery.value }
            .debounce(275)
            .distinctUntilChanged(),
        _filterState
    ) { searchText, filtersState ->
        if (searchText.isEmpty() && filtersState == InitialFiltersState) {
            coroutineScope {
                val freeGames = async {
                    gamesRepository.queryFreeGames(
                        FreeGamesQueryParameters(
                            platform = FreeGamesPlatform.pc,
                            sortingCriteria = FreeGamesSortingCriteria.relevance,
                            categories = null
                        )
                    )
                }
                val trendingGames = async {
                    gamesRepository.queryRichInfoGames(
                        RichInfoGamesQuery(
                            pageSize = 20,
                            dates = listOf(
                                LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE),
                                LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                            ),
                            metaCriticScores = listOf(85, 100),
                            sortingCriteria = RichInfoGamesSortingCriteria.ReleasedDescending
                        )
                    )
                }
                val topRatedGames = async {
                    gamesRepository.queryRichInfoGames(
                        RichInfoGamesQuery(
                            pageSize = 20,
                            metaCriticScores = listOf(95, 100),
                            sortingCriteria = RichInfoGamesSortingCriteria.RatingDescending
                        )
                    )
                }
                val multiplayerGames = async {
                    gamesRepository.queryRichInfoGames(
                        RichInfoGamesQuery(
                            pageSize = 20,
                            genres = listOf(59),
                            sortingCriteria = RichInfoGamesSortingCriteria.RatingDescending
                        )
                    )
                }
                val familyGames = async {
                    gamesRepository.queryRichInfoGames(
                        RichInfoGamesQuery(
                            pageSize = 20,
                            genres = listOf(19),
                            sortingCriteria = RichInfoGamesSortingCriteria.RatingDescending
                        )
                    )
                }
                val realisticGames = async {
                    gamesRepository.queryRichInfoGames(
                        RichInfoGamesQuery(
                            pageSize = 20,
                            tags = listOf(77),
                            sortingCriteria = RichInfoGamesSortingCriteria.RatingDescending
                        )
                    )
                }
                listOf(
                    freeGames,
                    trendingGames,
                    topRatedGames,
                    multiplayerGames,
                    familyGames,
                    realisticGames
                ).awaitAll().let {
                    DiscoverStateGames.SuggestedGames(
                        freeGames = (it[0] as Result<List<FreeGame>, Throwable>).wrapResultResources(),
                        trendingGames = (it[1] as Result<RichInfoGamesPage, Throwable>).wrapResultResources(),
                        topRatedGames = (it[2] as Result<RichInfoGamesPage, Throwable>).wrapResultResources(),
                        multiplayerGames = (it[3] as Result<RichInfoGamesPage, Throwable>).wrapResultResources(),
                        familyGames = (it[4] as Result<RichInfoGamesPage, Throwable>).wrapResultResources(),
                        realisticGames = (it[5] as Result<RichInfoGamesPage, Throwable>).wrapResultResources()
                    )
                }
            }
        } else {
            gamesRepository.queryRichInfoGames(
                RichInfoGamesQuery(
                    searchQuery = searchText.takeIf { it.isNotEmpty() && it.isNotBlank() },
                    isFuzzinessEnabled = true,
                    matchTermsExactly = false,
                    platforms = filtersState.selectedPlatforms.map { it.id }.takeIf { it.isNotEmpty() },
                    stores = filtersState.selectedStores.map { it.id }.takeIf { it.isNotEmpty() },
                    genres = filtersState.selectedGenres.map { it.id }.takeIf { it.isNotEmpty() },
                    sortingCriteria = run {
                        val sortingCriteria = filtersState.sortingCriteria ?: return@run null
                        when (sortingCriteria.criteria) {
                            Criteria.Name -> {
                                if (sortingCriteria.order == Order.Ascending) {
                                    RichInfoGamesSortingCriteria.NameAscending
                                } else {
                                    RichInfoGamesSortingCriteria.NameDescending
                                }
                            }
                            Criteria.DateReleased -> {
                                if (sortingCriteria.order == Order.Ascending) {
                                    RichInfoGamesSortingCriteria.ReleasedAscending
                                } else {
                                    RichInfoGamesSortingCriteria.ReleasedDescending
                                }
                            }
                            Criteria.DateAdded -> {
                                if (sortingCriteria.order == Order.Ascending) {
                                    RichInfoGamesSortingCriteria.DateAddedAscending
                                } else {
                                    RichInfoGamesSortingCriteria.DateAddedDescending
                                }
                            }
                            Criteria.DateCreated -> {
                                if (sortingCriteria.order == Order.Ascending) {
                                    RichInfoGamesSortingCriteria.DateCreatedAscending
                                } else {
                                    RichInfoGamesSortingCriteria.DateCreatedDescending
                                }
                            }
                            Criteria.DateUpdated -> {
                                if (sortingCriteria.order == Order.Ascending) {
                                    RichInfoGamesSortingCriteria.DateUpdatedAscending
                                } else {
                                    RichInfoGamesSortingCriteria.DateUpdatedDescending
                                }
                            }
                            Criteria.Rating -> {
                                if (sortingCriteria.order == Order.Ascending) {
                                    RichInfoGamesSortingCriteria.RatingAscending
                                } else {
                                    RichInfoGamesSortingCriteria.RatingDescending
                                }
                            }
                            Criteria.Metascore -> {
                                if (sortingCriteria.order == Order.Ascending) {
                                    RichInfoGamesSortingCriteria.MetacriticScoreAscending
                                } else {
                                    RichInfoGamesSortingCriteria.MetacriticScoreDescending
                                }
                            }
                        }
                    }
                )
            ).let {
                DiscoverStateGames.SearchQueryBasedGames(
                    richInfoGames = it.wrapResultResources()
                )
            }
        }
    }

    val discoverState = combine(
        snapshotFlow { searchQuery.value },
        _filterState,
        games
    ) { searchText, filters, games ->
        DiscoverState(
            searchQuery = searchText,
            filtersState = filters,
            games = games
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        Initial
    )

    fun updateSearchQuery(searchQueryText: String) {
        Snapshot.withMutableSnapshot {
            searchQuery.value = searchQueryText
        }
    }

    fun addToSelectedPlatforms(platform: Platform) {
        _filterState.update { it.copy(selectedPlatforms = it.selectedPlatforms + platform) }
    }

    fun removeFromSelectedPlatforms(platform: Platform) {
        _filterState.update { it.copy(selectedPlatforms = it.selectedPlatforms - platform) }
    }

    fun addToSelectedStores(store: Store) {
        _filterState.update { it.copy(selectedStores = it.selectedStores + store) }
    }

    fun removeFromSelectedStores(store: Store) {
        _filterState.update { it.copy(selectedStores = it.selectedStores - store) }
    }

    fun addToSelectedGenres(genre: Genre) {
        _filterState.update { it.copy(selectedGenres = it.selectedGenres + genre) }
    }

    fun removeFromSelectedGenres(genre: Genre) {
        _filterState.update { it.copy(selectedGenres = it.selectedGenres - genre) }
    }

    companion object {
        val InitialFiltersState = DiscoverFiltersState(
            0,
            allPlatforms = setOf(
                Platform(id = 21, label = "Android"),
                Platform(id = 3, label = "IOS"),
                Platform(id = 27, label = "Playstation 1"),
                Platform(id = 15, label = "Playstation 2"),
                Platform(id = 16, label = "Playstation 3"),
                Platform(id = 18, label = "Playstation 4"),
                Platform(id = 187, label = "Playstation 5"),
                Platform(id = 1, label = "Xbox One"),
                Platform(id = 186, label = "Xbox Series S/X"),
                Platform(id = 4, label = "PC")
            ),
            selectedPlatforms = emptySet(),
            allStores = setOf(
                Store(id = 1, label = "Steam"),
                Store(id = 4, label = "App store"),
                Store(id = 8, label = "Google play"),
                Store(id = 3, label = "Playstation store"),
                Store(id = 2, label = "Xbox store"),
                Store(id = 5, label = "GOG"),
                Store(id = 6, label = "Nintendo store"),
                Store(id = 9, label = "Itch.io"),
                Store(id = 11, label = "Epic games")
            ),
            selectedStores = emptySet(),
            allGenres = setOf(
                Genre(id = 4, label = "Action"),
                Genre(id = 51, label = "Indie"),
                Genre(id = 3, label = "Adventure"),
                Genre(id = 5, label = "RPG"),
                Genre(id = 10, label = "Strategy"),
                Genre(id = 2, label = "Shooter"),
                Genre(id = 40, label = "Casual"),
                Genre(id = 14, label = "Simulation"),
                Genre(id = 7, label = "Puzzle"),
                Genre(id = 11, label = "Arcade"),
                Genre(id = 83, label = "Platformer"),
                Genre(id = 1, label = "Racing"),
                Genre(id = 15, label = "Sports"),
                Genre(id = 6, label = "Fighting"),
                Genre(id = 19, label = "Family"),
                Genre(id = 59, label = "Massively Multiplayer")
            ),
            selectedGenres = emptySet(),
            sortingCriteria = null
        )

        val Initial = DiscoverState(
            "",
            InitialFiltersState,
            games = DiscoverStateGames.SuggestedGames(
                freeGames = Result.Success(placeholders()),
                trendingGames = Result.Success(placeholders()),
                topRatedGames = Result.Success(placeholders()),
                multiplayerGames = Result.Success(placeholders()),
                familyGames = Result.Success(placeholders()),
                realisticGames = Result.Success(placeholders())
            )
        )

        private fun placeholders(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}

private fun Result<RichInfoGamesPage, Throwable>.wrapResultResources(): Result<List<ResourceWrapper<RichInfoGame>>, Throwable> {
    return when (this) {
        is Result.Success -> Result.Success(data.games.map(RichInfoGame::wrapResource))
        is Result.Error -> this
    }
}
