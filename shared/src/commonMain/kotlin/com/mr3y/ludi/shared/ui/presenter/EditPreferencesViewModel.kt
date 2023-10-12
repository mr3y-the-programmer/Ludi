package com.mr3y.ludi.shared.ui.presenter

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Source
import com.mr3y.ludi.shared.core.model.onSuccess
import com.mr3y.ludi.shared.core.repository.GamesRepository
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.core.model.mapTypeToIconRes
import com.mr3y.ludi.shared.ui.datastore.ProtoDataStoreMutator
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.model.EditPreferencesState
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.shared.ui.presenter.model.SupportedNewsDataSources
import com.mr3y.ludi.shared.ui.presenter.model.toFollowedNewsDataSource
import com.mr3y.ludi.shared.ui.presenter.model.toUserFavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.toUserFavouriteGenre
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
    private val protoDataStore: ProtoDataStoreMutator,
    @Assisted private val preferencesType: PreferencesType
) : ScreenModel {

    private val userFollowedNewsSources = protoDataStore.followedNewsDataSources
        .catch {
            emit(FollowedNewsDataSources())
        }
        .map {
            it.newsDataSource.map { followedDataSource ->
                NewsDataSource(name = followedDataSource.name, type = Source.valueOf(followedDataSource.type), iconResName = mapTypeToIconRes(Source.valueOf(followedDataSource.type)))
            }
        }

    private val userFavGames = protoDataStore.favouriteGames
        .catch {
            emit(UserFavouriteGames())
        }
        .map {
            it.favGame.map { game ->
                FavouriteGame(id = game.id, title = game.name, imageUrl = game.thumbnailUrl, rating = game.rating)
            }
        }

    private val allGenres = flow {
        emit(gamesRepository.queryGamesGenres().onSuccess { page -> page.genres })
    }

    private val userFavGenres = protoDataStore.favouriteGenres
        .catch {
            emit(UserFavouriteGenres())
        }
        .map {
            it.favGenre.map { genre ->
                GameGenre(genre.id, genre.name, genre.slug, genre.gamesCount, genre.imageUrl)
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
            protoDataStore.unFollowNewsDataSource(source.toFollowedNewsDataSource())
        }
    }

    fun followNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            protoDataStore.followNewsDataSource(source.toFollowedNewsDataSource())
        }
    }

    fun removeGameFromFavourites(game: FavouriteGame) {
        coroutineScope.launch {
            protoDataStore.removeGameFromFavourites(game.toUserFavouriteGame())
        }
    }

    fun addToSelectedGenres(genre: GameGenre) {
        coroutineScope.launch {
            protoDataStore.addGenreToFavourites(genre.toUserFavouriteGenre())
        }
    }

    fun removeFromSelectedGenres(genre: GameGenre) {
        coroutineScope.launch {
            protoDataStore.removeGenreFromFavourites(genre.toUserFavouriteGenre())
        }
    }

    companion object {
        val InitialState = EditPreferencesState.Undefined
    }
}
