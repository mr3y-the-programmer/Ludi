package com.mr3y.ludi.shared.ui.presenter.model

import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Result

sealed interface EditPreferencesState {
    @JvmInline
    value class FavouriteGames(val favouriteGames: List<FavouriteGame>) : EditPreferencesState
    data class FollowedNewsDataSources(val allNewsDataSources: List<NewsDataSource>, val followedNewsDataSources: List<NewsDataSource>) : EditPreferencesState
    data class FavouriteGenres(val allGenres: Result<Set<GameGenre>, Throwable>, val favouriteGenres: Set<GameGenre>) : EditPreferencesState
    object Undefined : EditPreferencesState
}
