package com.mr3y.ludi.ui.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.ui.presenter.model.DiscoverState
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.Platform
import com.mr3y.ludi.ui.presenter.model.Store
import com.mr3y.ludi.ui.presenter.model.Tag
import com.mr3y.ludi.ui.presenter.model.TaggedGames
import com.mr3y.ludi.ui.presenter.usecases.GetSearchQueryBasedGamesUseCase
import com.mr3y.ludi.ui.presenter.usecases.GetSuggestedGamesUseCase
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
    private val getSuggestedGamesUseCase: GetSuggestedGamesUseCase,
    private val searchQueryBasedGamesUseCase: GetSearchQueryBasedGamesUseCase
) : ScreenModel {

    private var searchQuery = mutableStateOf("")

    private val _filterState = MutableStateFlow(InitialFiltersState)

    private val _triggerLoadingNewSuggestedGames = MutableStateFlow(0)

    private val refreshing = MutableStateFlow(0)

    private var _internalState by mutableStateOf(Initial)

    private val games = combine(
        snapshotFlow { searchQuery.value }
            .debounce(275)
            .distinctUntilChanged(),
        _filterState,
        _triggerLoadingNewSuggestedGames,
        refreshing
    ) { searchText, filtersState, page, _ ->
        if (searchText.isEmpty() && filtersState == InitialFiltersState) {
            getSuggestedGamesUseCase(page.coerceAtLeast(0))
        } else {
            searchQueryBasedGamesUseCase(searchText, filtersState)
        }
    }.map {
        Snapshot.withMutableSnapshot { _internalState = _internalState.copy(isRefreshing = false, gamesState = it) }
    }.launchIn(coroutineScope)

    val discoverState = combine(
        _filterState,
        snapshotFlow { _internalState }
    ) { filters, state ->
        Snapshot.withMutableSnapshot {
            _internalState = state.copy(filtersState = filters)
        }
        _internalState
    }.stateIn(
        coroutineScope,
        SharingStarted.Lazily,
        _internalState
    )

    fun updateSearchQuery(searchQueryText: String) {
        Snapshot.withMutableSnapshot {
            searchQuery.value = searchQueryText
            _internalState = _internalState.copy(searchQuery = searchQueryText)
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

    fun addToSelectedTags(tag: Tag) {
        _filterState.update { it.copy(selectedTags = it.selectedTags + tag) }
    }

    fun removeFromSelectedTags(tag: Tag) {
        _filterState.update { it.copy(selectedTags = it.selectedTags - tag) }
    }

    fun loadNewSuggestedGames() {
        _triggerLoadingNewSuggestedGames.update { it + 1 }
    }

    fun refresh() {
        refreshing.update { it + 1 }
        _triggerLoadingNewSuggestedGames.update { 0 }
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

        val Initial = DiscoverState(
            searchQuery = "",
            filtersState = InitialFiltersState,
            gamesState = DiscoverStateGames.SuggestedGames(
                taggedGamesList = listOf(
                    TaggedGames.TrendingGames(Result.Loading),
                    TaggedGames.TopRatedGames(Result.Loading),
                    TaggedGames.MultiplayerGames(Result.Loading)
                )
            ),
            isRefreshing = true
        )
    }
}
