package com.mr3y.ludi.ui.presenter

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.GamesPage
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.query.GamesQuery
import com.mr3y.ludi.core.repository.query.GamesSortingCriteria
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
                val trendingGames = async {
                    gamesRepository.queryGames(
                        GamesQuery(
                            pageSize = 20,
                            dates = listOf(
                                LocalDate.now().minusYears(1).format(DateTimeFormatter.ISO_DATE),
                                LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                            ),
                            metaCriticScores = listOf(85, 100),
                            sortingCriteria = GamesSortingCriteria.ReleasedDescending
                        )
                    )
                }
                val topRatedGames = async {
                    gamesRepository.queryGames(
                        GamesQuery(
                            pageSize = 20,
                            metaCriticScores = listOf(95, 100),
                            sortingCriteria = GamesSortingCriteria.RatingDescending
                        )
                    )
                }
                val multiplayerGames = async {
                    gamesRepository.queryGames(
                        GamesQuery(
                            pageSize = 20,
                            genres = listOf(59),
                            sortingCriteria = GamesSortingCriteria.RatingDescending
                        )
                    )
                }
                val familyGames = async {
                    gamesRepository.queryGames(
                        GamesQuery(
                            pageSize = 20,
                            genres = listOf(19),
                            sortingCriteria = GamesSortingCriteria.RatingDescending
                        )
                    )
                }
                val realisticGames = async {
                    gamesRepository.queryGames(
                        GamesQuery(
                            pageSize = 20,
                            tags = listOf(77),
                            sortingCriteria = GamesSortingCriteria.RatingDescending
                        )
                    )
                }
                listOf(
                    trendingGames,
                    topRatedGames,
                    multiplayerGames,
                    familyGames,
                    realisticGames
                ).awaitAll().let {
                    DiscoverStateGames.SuggestedGames(
                        trendingGames = it[0].wrapResultResources(),
                        topRatedGames = it[1].wrapResultResources(),
                        multiplayerGames = it[2].wrapResultResources(),
                        familyGames = it[3].wrapResultResources(),
                        realisticGames = it[4].wrapResultResources()
                    )
                }
            }
        } else {
            gamesRepository.queryGames(
                GamesQuery(
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
                    games = it.groupByGenreAndWrapResources()
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

private fun Result<GamesPage, Throwable>.wrapResultResources(): Result<List<ResourceWrapper<Game>>, Throwable> {
    return when (this) {
        is Result.Success -> Result.Success(data.games.map(Game::wrapResource))
        is Result.Error -> this
    }
}

private fun Result<GamesPage, Throwable>.groupByGenreAndWrapResources(): Result<ResourceWrapper<Map<GameGenre, List<Game>>>, Throwable> {
    return when (this) {
        is Result.Success -> Result.Success(ResourceWrapper.ActualResource(data.games.groupByGenre()))
        is Result.Error -> this
    }
}

internal fun List<Game>.groupByGenre(): Map<GameGenre, List<Game>> {
    if (isEmpty()) {
        return emptyMap()
    }
    val otherGenreGames = mutableListOf<Game>()
    val otherGenre = GameGenre(
        id = Int.MAX_VALUE,
        name = "Other",
        slug = "other",
        gamesCount = Long.MAX_VALUE,
        imageUrl = null
    )
    val alreadyAssociated = mutableListOf<Game>()
    val genres = this.mapNotNull { it.genres.firstOrNull() }.toSet()
    if (genres.isEmpty()) {
        return mapOf(otherGenre.copy(gamesCount = size.toLong()) to this)
    }
    val gamesGroupedByGenre = genres.associateWith { genre ->
        (this - alreadyAssociated.toSet()).filter {
            val gameFirstGenre = it.genres.firstOrNull()
            if (gameFirstGenre != null) {
                val matched = gameFirstGenre == genre
                if (matched) {
                    alreadyAssociated += it
                }
                matched
            } else {
                otherGenreGames += it
                return@filter false
            }
        }
    }
    return if (otherGenreGames.isNotEmpty()) {
        gamesGroupedByGenre + Pair(otherGenre, otherGenreGames)
    } else {
        gamesGroupedByGenre
    }
}
