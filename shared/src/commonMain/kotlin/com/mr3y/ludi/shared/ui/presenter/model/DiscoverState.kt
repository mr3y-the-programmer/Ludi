package com.mr3y.ludi.shared.ui.presenter.model

import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Result

data class DiscoverState(
    val searchQuery: String,
    val filtersState: DiscoverFiltersState,
    val gamesState: DiscoverStateGames,
    val isRefreshing: Boolean
)

sealed interface DiscoverStateGames {

    data class SuggestedGames(val taggedGamesList: List<TaggedGames>) : DiscoverStateGames

    data class SearchQueryBasedGames(
        val games: Result<Map<GameGenre, List<Game>>, Throwable>
    ) : DiscoverStateGames
}

sealed interface TaggedGames {
    val games: Result<List<Game>, Throwable>

    data class TrendingGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class TopRatedGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class BasedOnFavGenresGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class MultiplayerGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class FreeGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class StoryGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class BoardGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class ESportsGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class RaceGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class PuzzleGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames

    data class SoundtrackGames(
        override val games: Result<List<Game>, Throwable>
    ) : TaggedGames
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
