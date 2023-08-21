package com.mr3y.ludi.ui.presenter

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.ui.presenter.model.DiscoverState
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.Genre
import com.mr3y.ludi.ui.presenter.model.Platform
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.Store
import com.mr3y.ludi.ui.presenter.model.TaggedGames
import com.mr3y.ludi.ui.presenter.usecases.GetSearchQueryBasedGamesUseCase
import com.mr3y.ludi.ui.presenter.usecases.GetSuggestedGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getSuggestedGamesUseCase: GetSuggestedGamesUseCase,
    private val searchQueryBasedGamesUseCase: GetSearchQueryBasedGamesUseCase
) : ViewModel() {

    private var searchQuery = mutableStateOf("")

    private val _filterState = MutableStateFlow(InitialFiltersState)

    private val _triggerLoadingNewSuggestedGames = MutableStateFlow(0)

    private val games = combine(
        snapshotFlow { searchQuery.value }
            .debounce(275)
            .distinctUntilChanged(),
        _filterState,
        _triggerLoadingNewSuggestedGames
    ) { searchText, filtersState, _ ->
        if (searchText.isEmpty() && filtersState == InitialFiltersState) {
            getSuggestedGamesUseCase()
        } else {
            searchQueryBasedGamesUseCase(searchText, filtersState)
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

    fun loadNewSuggestedGames() {
        _triggerLoadingNewSuggestedGames.update { it + 1 }
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
            searchQuery = "",
            filtersState = InitialFiltersState,
            games = DiscoverStateGames.SuggestedGames(
                taggedGamesList = listOf(
                    TaggedGames.TrendingGames(Result.Success(placeholders())),
                    TaggedGames.TopRatedGames(Result.Success(placeholders())),
                    TaggedGames.MultiplayerGames(Result.Success(placeholders()))
                )
            )
        )

        private fun placeholders(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}
