package com.mr3y.ludi.shared.ui.datastore

import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import kotlinx.coroutines.flow.Flow

interface ProtoDataStoreMutator {

    val followedNewsDataSources: Flow<FollowedNewsDataSources>

    val favouriteGames: Flow<UserFavouriteGames>

    val favouriteGenres: Flow<UserFavouriteGenres>

    suspend fun followNewsDataSource(source: FollowedNewsDataSource)

    suspend fun unFollowNewsDataSource(source: FollowedNewsDataSource)

    suspend fun addGameToFavourites(game: UserFavouriteGame)

    suspend fun removeGameFromFavourites(game: UserFavouriteGame)

    suspend fun addGenreToFavourites(genre: UserFavouriteGenre)

    suspend fun removeGenreFromFavourites(genre: UserFavouriteGenre)
}
