package com.mr3y.ludi.shared.ui.presenter

import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGame
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenre
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.ui.datastore.ProtoDataStoreMutator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FakeProtoDataStoreMutator : ProtoDataStoreMutator {

    private val _followedNewsDataSources = mutableListOf<FollowedNewsDataSources>()
    override val followedNewsDataSources: Flow<FollowedNewsDataSources> = _followedNewsDataSources.asFlow()

    private val _favouriteGames = mutableListOf<UserFavouriteGames>()
    override val favouriteGames: Flow<UserFavouriteGames> = _favouriteGames.asFlow()

    private val _favouriteGenres = mutableListOf<UserFavouriteGenres>()
    override val favouriteGenres: Flow<UserFavouriteGenres> = _favouriteGenres.asFlow()

    override suspend fun followNewsDataSource(source: FollowedNewsDataSource) {
        _followedNewsDataSources += FollowedNewsDataSources(newsDataSource = listOf(source))
    }

    override suspend fun unFollowNewsDataSource(source: FollowedNewsDataSource) {
        val item = _followedNewsDataSources.firstOrNull { it.newsDataSource.contains(source) } ?: return
        _followedNewsDataSources -= item
    }

    override suspend fun addGameToFavourites(game: UserFavouriteGame) {
        _favouriteGames += UserFavouriteGames(favGame = listOf(game))
    }

    override suspend fun removeGameFromFavourites(game: UserFavouriteGame) {
        val item = _favouriteGames.firstOrNull { it.favGame.contains(game) } ?: return
        _favouriteGames -= item
    }

    override suspend fun addGenreToFavourites(genre: UserFavouriteGenre) {
        _favouriteGenres += UserFavouriteGenres(favGenre = listOf(genre))
    }

    override suspend fun removeGenreFromFavourites(genre: UserFavouriteGenre) {
        val item = _favouriteGenres.firstOrNull { it.favGenre.contains(genre) } ?: return
        _favouriteGenres -= item
    }

    fun clearAll() {
        _followedNewsDataSources.clear()
        _favouriteGames.clear()
        _favouriteGenres.clear()
    }
}
