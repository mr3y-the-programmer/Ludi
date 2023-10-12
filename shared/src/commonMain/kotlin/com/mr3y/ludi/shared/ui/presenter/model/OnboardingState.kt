package com.mr3y.ludi.shared.ui.presenter.model

import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.Source

data class OnboardingState(
    val allNewsDataSources: List<NewsDataSource>,
    val followedNewsDataSources: List<NewsDataSource>,
    val searchQuery: String,
    val onboardingGames: OnboardingGames,
    val isRefreshingGames: Boolean,
    val favouriteGames: List<FavouriteGame>,
    val allGamingGenres: Result<Set<GameGenre>, Throwable>,
    val isRefreshingGenres: Boolean,
    val selectedGamingGenres: Set<GameGenre>
)

sealed interface OnboardingGames {
    val games: Result<List<Game>, Throwable>

    data class SuggestedGames(override val games: Result<List<Game>, Throwable>) : OnboardingGames
    data class SearchQueryBasedGames(override val games: Result<List<Game>, Throwable>) : OnboardingGames
}

data class NewsDataSource(
    val name: String,
    val iconResName: String,
    val type: Source
)

internal fun NewsDataSource.toFollowedNewsDataSource(): FollowedNewsDataSource {
    return FollowedNewsDataSource(name = name, drawableId = 0, type = type.name)
}

data class FavouriteGame(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val rating: Float
)

internal fun FavouriteGame.toUserFavouriteGame(): UserFavouriteGame {
    return UserFavouriteGame(id = id, name = title, thumbnailUrl = imageUrl, rating = rating)
}

internal fun GameGenre.toUserFavouriteGenre(): UserFavouriteGenre {
    return UserFavouriteGenre(id = id, name = name, imageUrl = imageUrl ?: "", slug = slug ?: "", gamesCount = gamesCount ?: 0L)
}

