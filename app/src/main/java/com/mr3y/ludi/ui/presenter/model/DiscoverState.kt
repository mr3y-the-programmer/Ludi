package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
data class DiscoverState(
    val searchQuery: String,
    val filtersState: DiscoverFiltersState,
    val gamesState: DiscoverStateGames
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
