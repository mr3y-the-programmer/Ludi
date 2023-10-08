package com.mr3y.ludi.shared.ui.presenter

import androidx.datastore.core.DataStore
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Source
import com.mr3y.ludi.shared.core.model.onSuccess
import com.mr3y.ludi.shared.core.repository.GamesRepository
import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.model.EditPreferencesState
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.shared.ui.presenter.model.SupportedNewsDataSources
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
            emit(FollowedNewsDataSources())
        }
        .map {
            it.newsDataSource.map { followedDataSource ->
                NewsDataSource(name = followedDataSource.name, drawableId = followedDataSource.drawableId, type = Source.valueOf(followedDataSource.type), iconResName = mapTypeToIconRes(Source.valueOf(followedDataSource.type)))
            }
        }

    private val userFavGames = favGamesStore.data
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

    private val userFavGenres = favGenresStore.data
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
            followedNewsDataSourcesStore.updateData {
                it.copy(newsDataSource = it.newsDataSource - source.toFollowedNewsDataSource())
            }
        }
    }

    fun followNewsDataSource(source: NewsDataSource) {
        coroutineScope.launch {
            followedNewsDataSourcesStore.updateData {
                val followedNewsDataSource = source.toFollowedNewsDataSource()
                if (followedNewsDataSource.type !in it.newsDataSource.map { source -> source.type }) {
                    it.copy(newsDataSource = it.newsDataSource + followedNewsDataSource)
                } else {
                    it
                }
            }
        }
    }

    fun removeGameFromFavourites(game: FavouriteGame) {
        coroutineScope.launch {
            favGamesStore.updateData {
                it.copy(favGame = it.favGame - game.toUserFavouriteGame())
            }
        }
    }

    fun addToSelectedGenres(genre: GameGenre) {
        coroutineScope.launch {
            favGenresStore.updateData {
                val favouriteGenre = genre.toUserFavouriteGenre()
                if (favouriteGenre.name !in it.favGenre.map { genre -> genre.name }) {
                    it.copy(favGenre = it.favGenre + favouriteGenre)
                } else {
                    it
                }
            }
        }
    }

    fun removeFromSelectedGenres(genre: GameGenre) {
        coroutineScope.launch {
            favGenresStore.updateData {
                it.copy(favGenre = it.favGenre - genre.toUserFavouriteGenre())
            }
        }
    }

    private fun mapTypeToIconRes(type: Source): String {
        return when(type) {
            Source.GiantBomb -> "giant_bomb_logo.xml"
            Source.GameSpot -> "game_spot_logo.xml"
            Source.IGN -> "ign_logo.xml"
            Source.TechRadar -> "tech_radar_logo.xml"
            Source.PCGamesN -> "pcgamesn_logo.xml"
            Source.RockPaperShotgun -> "rockpapershotgun_logo.xml"
            Source.PCInvasion -> "pcinvasion_logo.xml"
            Source.GloriousGaming -> "gloriousgaming_logo.xml"
            Source.EuroGamer -> "eurogamer_logo.xml"
            Source.VG247 -> "vg247_logo.xml"
            Source.TheGamer -> "thegamer_logo.xml"
            Source.GameRant -> "gamerant_logo.xml"
            Source.BrutalGamer -> "brutalgamer_logo.xml"
            Source.VentureBeat -> "venturebeat_logo.xml"
            Source.Polygon -> "polygon_logo.xml"
            Source.PCGamer -> "pcgamer_logo.xml"
        }
    }

    companion object {
        val InitialState = EditPreferencesState.Undefined
    }
}

private fun NewsDataSource.toFollowedNewsDataSource(): FollowedNewsDataSource {
    return FollowedNewsDataSource(name = name, drawableId = drawableId ?: 0, type = type.name)
}

private fun FavouriteGame.toUserFavouriteGame(): UserFavouriteGame {
    return UserFavouriteGame(id = id, name = title, thumbnailUrl = imageUrl, rating = rating)
}

private fun GameGenre.toUserFavouriteGenre(): UserFavouriteGenre {
    return UserFavouriteGenre(id = id, name = name, imageUrl = imageUrl ?: "", slug = slug ?: "", gamesCount = gamesCount ?: 0L)
}
