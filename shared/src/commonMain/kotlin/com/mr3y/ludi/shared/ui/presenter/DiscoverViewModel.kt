package com.mr3y.ludi.shared.ui.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverState
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.shared.ui.presenter.model.Platform
import com.mr3y.ludi.shared.ui.presenter.model.Store
import com.mr3y.ludi.shared.ui.presenter.model.Tag
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject

@OptIn(FlowPreview::class)
@Inject
class DiscoverViewModel(
    private val pagingFactory: DiscoverPagingFactory
) : ScreenModel, DiscoverPagingFactory by pagingFactory {

    val searchQuery = mutableStateOf("")

    private val _filterState = MutableStateFlow(InitialFiltersState)

    private val refreshing = MutableStateFlow(0)

    private val trendingGames = trendingGamesPager.cachedIn(coroutineScope)

    private val topRatedGames = topRatedGamesPager.cachedIn(coroutineScope)

    private val favGenresBasedGames = favGenresBasedGamesPager?.cachedIn(coroutineScope)

    private val multiplayerGames = multiplayerGamesPager.cachedIn(coroutineScope)

    private val freeGames = freeGamesPager.cachedIn(coroutineScope)

    private val storyGames = storyGamesPager.cachedIn(coroutineScope)

    private val boardGames = boardGamesPager.cachedIn(coroutineScope)

    private val esportsGames = esportsGamesPager.cachedIn(coroutineScope)

    private val raceGames = raceGamesPager.cachedIn(coroutineScope)

    private val puzzleGames = puzzleGamesPager.cachedIn(coroutineScope)

    private val soundtrackGames = soundtrackGamesPager.cachedIn(coroutineScope)

    internal val Initial = DiscoverState(
        filtersState = InitialFiltersState,
        gamesState = DiscoverStateGames.SuggestedGames(
            trendingGames = trendingGames,
            topRatedGames = topRatedGames,
            favGenresBasedGames = favGenresBasedGames,
            multiplayerGames = multiplayerGames,
            freeGames = freeGames,
            storyGames = storyGames,
            boardGames = boardGames,
            eSportsGames = esportsGames,
            raceGames = raceGames,
            puzzleGames = puzzleGames,
            soundtrackGames = soundtrackGames
        ),
        isRefreshing = true
    )

    private var _internalState by mutableStateOf(Initial)

    private val games = combine(
        snapshotFlow { searchQuery.value }
            .debounce(275)
            .distinctUntilChanged(),
        _filterState,
        refreshing
    ) { searchText, filtersState, _ ->
        if (searchText.isEmpty() && filtersState == InitialFiltersState) {
            Initial.gamesState
        } else {
            DiscoverStateGames.SearchQueryBasedGames(
                games = searchQueryBasedGamesPager(searchText, filtersState).cachedIn(coroutineScope)
            )
        }
    }.map {
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isRefreshing = false, gamesState = it) }
    }.launchIn(coroutineScope)

    val discoverState =
        snapshotFlow { _internalState }
            .stateIn(
                coroutineScope,
                SharingStarted.Lazily,
                _internalState
            )

    fun updateSearchQuery(searchQueryText: String) {
        Snapshot.withMutableSnapshot {
            searchQuery.value = searchQueryText
        }
    }

    fun addToSelectedPlatforms(platform: Platform) {
        _filterState.update { it.copy(selectedPlatforms = it.selectedPlatforms + platform) }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(filtersState = _filterState.value) }
    }

    fun removeFromSelectedPlatforms(platform: Platform) {
        _filterState.update { it.copy(selectedPlatforms = it.selectedPlatforms - platform) }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(filtersState = _filterState.value) }
    }

    fun addToSelectedStores(store: Store) {
        _filterState.update { it.copy(selectedStores = it.selectedStores + store) }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(filtersState = _filterState.value) }
    }

    fun removeFromSelectedStores(store: Store) {
        _filterState.update { it.copy(selectedStores = it.selectedStores - store) }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(filtersState = _filterState.value) }
    }

    fun addToSelectedTags(tag: Tag) {
        _filterState.update { it.copy(selectedTags = it.selectedTags + tag) }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(filtersState = _filterState.value) }
    }

    fun removeFromSelectedTags(tag: Tag) {
        _filterState.update { it.copy(selectedTags = it.selectedTags - tag) }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(filtersState = _filterState.value) }
    }

    fun refresh() {
        refreshing.update { it + 1 }
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isRefreshing = true) }
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
            allTags = setOf(
                Tag(id = 83, label = "Puzzle"),
                Tag(id = 42, label = "Soundtrack"),
                Tag(id = 1407, label = "Race"),
                Tag(id = 73, label = "ESports"),
                Tag(id = 162, label = "Board"),
                Tag(id = 406, label = "Story"),
                Tag(id = 79, label = "Free"),
                Tag(id = 31, label = "Single Player"),
                Tag(id = 7, label = "Multiplayer"),
                Tag(id = 397, label = "Online Multiplayer"),
                Tag(id = 468, label = "Role Playing"),
                Tag(id = 36, label = "Open World"),
                Tag(id = 32, label = "Sci-fi"),
                Tag(id = 45, label = "2D"),
                Tag(id = 123, label = "Comedy"),
                Tag(id = 134, label = "Anime")
            ).toSortedSet(compareBy { it.label }),
            selectedTags = emptySet(),
            sortingCriteria = null
        )
    }
}
