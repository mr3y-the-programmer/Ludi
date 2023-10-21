package com.mr3y.ludi.shared.ui.presenter.model

import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.model.Game
import kotlinx.coroutines.flow.Flow

data class DiscoverState(
    val filtersState: DiscoverFiltersState,
    val gamesState: DiscoverStateGames,
    val isRefreshing: Boolean
)

sealed interface DiscoverStateGames {

    data class SuggestedGames(
        val trendingGames: Flow<PagingData<Game>>,
        val topRatedGames: Flow<PagingData<Game>>,
        val favGenresBasedGames: Flow<PagingData<Game>>?,
        val multiplayerGames: Flow<PagingData<Game>>,
        val freeGames: Flow<PagingData<Game>>,
        val storyGames: Flow<PagingData<Game>>,
        val boardGames: Flow<PagingData<Game>>,
        val eSportsGames: Flow<PagingData<Game>>,
        val raceGames: Flow<PagingData<Game>>,
        val puzzleGames: Flow<PagingData<Game>>,
        val soundtrackGames: Flow<PagingData<Game>>
    ) : DiscoverStateGames

    data class SearchQueryBasedGames(
        val games: Flow<PagingData<Game>>
    ) : DiscoverStateGames
}

data class DiscoverFiltersState(
    val currentPage: Int,
    val allPlatforms: Set<Platform>,
    val selectedPlatforms: Set<Platform>,
    val allStores: Set<Store>,
    val selectedStores: Set<Store>,
    val allTags: Set<Tag>,
    val selectedTags: Set<Tag>,
    val sortingCriteria: SortingCriteria?
)

data class Platform(
    val id: Int,
    val label: String
)

data class Store(
    val id: Int,
    val label: String
)

data class Tag(
    val id: Int,
    val label: String
)

data class SortingCriteria(
    val criteria: Criteria,
    val order: Order
)

enum class Criteria {
    Name,
    DateReleased,
    DateAdded,
    DateCreated,
    DateUpdated,
    Rating,
    Metascore
}
enum class Order {
    Ascending,
    Descending
}
