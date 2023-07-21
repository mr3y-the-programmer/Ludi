package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
data class DiscoverState(
    val searchQuery: String,
    val filtersState: DiscoverFiltersState,
    val games: DiscoverStateGames
)

sealed interface DiscoverStateGames {

    data class SuggestedGames(
        val trendingGames: Result<List<ResourceWrapper<Game>>, Throwable>,
        val topRatedGames: Result<List<ResourceWrapper<Game>>, Throwable>,
        val multiplayerGames: Result<List<ResourceWrapper<Game>>, Throwable>,
        val familyGames: Result<List<ResourceWrapper<Game>>, Throwable>,
        val realisticGames: Result<List<ResourceWrapper<Game>>, Throwable>
    ) : DiscoverStateGames
    data class SearchQueryBasedGames(
        val games: Result<ResourceWrapper<Map<GameGenre, List<Game>>>, Throwable>
    ) : DiscoverStateGames
}

data class DiscoverFiltersState(
    val currentPage: Int,
    val allPlatforms: Set<Platform>,
    val selectedPlatforms: Set<Platform>,
    val allStores: Set<Store>,
    val selectedStores: Set<Store>,
    val allGenres: Set<Genre>,
    val selectedGenres: Set<Genre>,
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

data class Genre(
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
