package com.mr3y.ludi.ui.presenter.model

import androidx.annotation.DrawableRes
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.Source

data class OnboardingState(
    val allNewsDataSources: List<NewsDataSource>,
    val followedNewsDataSources: List<NewsDataSource>,
    val isUpdatingFollowedNewsDataSources: Boolean,
    val searchQuery: String,
    val onboardingGames: OnboardingGames,
    val favouriteGames: List<FavouriteGame>,
    val isUpdatingFavouriteGames: Boolean,
    val allGamingGenres: Result<Set<GameGenre>, Throwable>,
    val selectedGamingGenres: Set<GameGenre>
)

sealed interface OnboardingGames {
    val games: Result<List<Game>, Throwable>

    data class SuggestedGames(override val games: Result<List<Game>, Throwable>) : OnboardingGames
    data class SearchQueryBasedGames(override val games: Result<List<Game>, Throwable>) : OnboardingGames
}

data class NewsDataSource(
    val name: String,
    @DrawableRes val drawableId: Int,
    val type: Source
)

data class FavouriteGame(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val rating: Float
)
