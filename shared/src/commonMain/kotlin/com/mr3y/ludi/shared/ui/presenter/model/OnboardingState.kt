package com.mr3y.ludi.shared.ui.presenter.model

import androidx.annotation.DrawableRes
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
    val type: Source,
    @DrawableRes val drawableId: Int? = null // This field exists for backward compatibility with current users' stored preferences
)

data class FavouriteGame(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val rating: Float
)
