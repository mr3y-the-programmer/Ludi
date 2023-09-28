package com.mr3y.ludi.ui.presenter

import androidx.datastore.core.DataStore
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.onSuccess
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.ui.navigation.PreferencesType
import com.mr3y.ludi.ui.presenter.model.EditPreferencesState
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.presenter.model.SupportedNewsDataSources
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class EditPreferencesViewModel(
    private val gamesRepository: GamesRepository,
    private val favGamesStore: DataStore<UserFavouriteGames>,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>,
    private val favGenresStore: DataStore<UserFavouriteGenres>,
    @Assisted private val preferencesType: PreferencesType
) : ScreenModel {

    private val userFollowedNewsSources = followedNewsDataSourcesStore.data
        .catch {
            emit(FollowedNewsDataSources.getDefaultInstance())
        }
        .map {
            it.newsDataSourceList.map { followedDataSource ->
                NewsDataSource(name = followedDataSource.name, drawableId = followedDataSource.drawableId, type = Source.valueOf(followedDataSource.type))
            }
        }

    private val userFavGames = favGamesStore.data
        .catch {
            emit(UserFavouriteGames.getDefaultInstance())
        }
        .map {
            it.favGameList.map { favGame ->
                FavouriteGame(id = favGame.id, title = favGame.name, imageUrl = favGame.thumbnailUrl, rating = favGame.rating)
            }
        }

    private val allGenres = flow {
        emit(gamesRepository.queryGamesGenres().onSuccess { page -> page.genres })
    }

    private val userFavGenres = favGenresStore.data
        .catch {
            emit(UserFavouriteGenres.getDefaultInstance())
        }
        .map {
            it.favGenreList.map { favGenre ->
                GameGenre(favGenre.id, favGenre.name, favGenre.slug, favGenre.gamesCount, favGenre.imageUrl)
            }
        }

    val editPreferencesState: StateFlow<EditPreferencesState> = combine(
        userFollowedNewsSources,
        userFavGames,
        allGenres,
        userFavGenres
    ) { sources, games, allGenres, favouriteGenres ->
        when (preferencesType) {
            PreferencesType.NewsDataSources ->
                EditPreferencesState.FollowedNewsDataSources(
                    allNewsDataSources = SupportedNewsDataSources,
                    followedNewsDataSources = sources
                )
            PreferencesType.Genres ->
                EditPreferencesState.FavouriteGenres(
                    allGenres = allGenres,
                    favouriteGenres = favouriteGenres.toSet()
                )
            PreferencesType.Games -> EditPreferencesState.FavouriteGames(games)
        }
    }.stateIn(
        coroutineScope,
        SharingStarted.Lazily,
        InitialState
    )

    fun unFollowNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            followedNewsDataSourcesStore.updateData {
                val sourceIndex = it.newsDataSourceList.indexOf(source.toFollowedNewsDataSource())
                // guard against cases where the user follows the source, and then unfollows it immediately after a few milliseconds.
                if (sourceIndex != -1) {
                    it.toBuilder().removeNewsDataSource(sourceIndex).build()
                } else {
                    it
                }
            }
        }
    }

    fun followNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            followedNewsDataSourcesStore.updateData {
                val followedNewsDataSource = source.toFollowedNewsDataSource()
                if (followedNewsDataSource.type !in it.newsDataSourceList.map { source -> source.type }) {
                    it.toBuilder().addNewsDataSource(followedNewsDataSource).build()
                } else {
                    it
                }
            }
        }
    }

    fun removeGameFromFavourites(game: FavouriteGame) {
        coroutineScope.launch {
            favGamesStore.updateData {
                val sourceIndex = it.favGameList.indexOf(game.toUserFavouriteGame())
                // guard against cases where the user adds the game to favourites, and then removes it immediately after a few milliseconds.
                if (sourceIndex != -1) {
                    it.toBuilder().removeFavGame(sourceIndex).build()
                } else {
                    it
                }
            }
        }
    }

    fun addToSelectedGenres(genre: GameGenre) {
        coroutineScope.launch {
            favGenresStore.updateData {
                val favouriteGenre = genre.toUserFavouriteGenre()
                if (favouriteGenre !in it.favGenreList) {
                    it.toBuilder().addFavGenre(favouriteGenre).build()
                } else {
                    it
                }
            }
        }
    }

    fun removeFromSelectedGenres(genre: GameGenre) {
        coroutineScope.launch {
            favGenresStore.updateData {
                val sourceIndex = it.favGenreList.indexOf(genre.toUserFavouriteGenre())
                // guard against cases where the user adds the genre to favourites, and then removes it immediately after a few milliseconds.
                if (sourceIndex != -1) {
                    it.toBuilder().removeFavGenre(sourceIndex).build()
                } else {
                    it
                }
            }
        }
    }

    companion object {
        val InitialState = EditPreferencesState.Undefined
    }
}

private fun NewsDataSource.toFollowedNewsDataSource(): FollowedNewsDataSource {
    return FollowedNewsDataSource.newBuilder()
        .setName(name)
        .setDrawableId(drawableId)
        .setType(type.name)
        .build()
}

private fun FavouriteGame.toUserFavouriteGame(): UserFavouriteGame {
    return UserFavouriteGame.newBuilder()
        .setId(id)
        .setName(title)
        .setThumbnailUrl(imageUrl)
        .setRating(rating)
        .build()
}

private fun GameGenre.toUserFavouriteGenre(): UserFavouriteGenre {
    return UserFavouriteGenre.newBuilder()
        .setId(id)
        .setName(name)
        .setSlug(slug)
        .setGamesCount(gamesCount ?: 0L)
        .setImageUrl(imageUrl)
        .build()
}
