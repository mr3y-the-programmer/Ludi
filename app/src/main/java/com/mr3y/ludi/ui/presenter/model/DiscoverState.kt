package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
data class DiscoverState(
    val searchQuery: String,
    val filtersState: DiscoverFiltersState,
    val games: DiscoverStateGames
)

sealed interface DiscoverStateGames {

    data class SuggestedGames(
        val trendingGames: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>,
        val topRatedGames: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>,
        val multiplayerGames: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>,
        val familyGames: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>,
        val realisticGames: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>
    ) : DiscoverStateGames
    data class SearchQueryBasedGames(
        val richInfoGames: Result<ResourceWrapper<Map<GameGenre, List<RichInfoGame>>>, Throwable>
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
