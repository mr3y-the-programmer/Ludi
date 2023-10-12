package com.mr3y.ludi.shared.ui.datastore.internal

import androidx.datastore.core.DataStore
import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.ui.datastore.ProtoDataStoreMutator
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class DefaultProtoDataStoreMutator(
    private val favGamesStore: DataStore<UserFavouriteGames>,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>,
    private val favGenresStore: DataStore<UserFavouriteGenres>,
) : ProtoDataStoreMutator {

    override val followedNewsDataSources: Flow<FollowedNewsDataSources> = followedNewsDataSourcesStore.data

    override val favouriteGames: Flow<UserFavouriteGames> = favGamesStore.data

    override val favouriteGenres: Flow<UserFavouriteGenres> = favGenresStore.data

    override suspend fun followNewsDataSource(source: FollowedNewsDataSource) {
        followedNewsDataSourcesStore.updateData {
            if (source.type !in it.newsDataSource.map { source -> source.type }) {
                it.copy(newsDataSource = it.newsDataSource + source)
            } else {
                it
            }
        }
    }

    override suspend fun unFollowNewsDataSource(source: FollowedNewsDataSource) {
        followedNewsDataSourcesStore.updateData {
            it.copy(newsDataSource = it.newsDataSource - source)
        }
    }

    override suspend fun addGameToFavourites(game: UserFavouriteGame) {
        favGamesStore.updateData {
            if (game !in it.favGame) {
                it.copy(favGame = it.favGame + game)
            } else {
                it
            }
        }
    }

    override suspend fun removeGameFromFavourites(game: UserFavouriteGame) {
        favGamesStore.updateData {
            it.copy(favGame = it.favGame - game)
        }
    }

    override suspend fun addGenreToFavourites(genre: UserFavouriteGenre) {
        favGenresStore.updateData {
            if (genre.name !in it.favGenre.map { genre -> genre.name }) {
                it.copy(favGenre = it.favGenre + genre)
            } else {
                it
            }
        }
    }

    override suspend fun removeGenreFromFavourites(genre: UserFavouriteGenre) {
        favGenresStore.updateData {
            it.copy(favGenre = it.favGenre - genre)
        }
    }
}