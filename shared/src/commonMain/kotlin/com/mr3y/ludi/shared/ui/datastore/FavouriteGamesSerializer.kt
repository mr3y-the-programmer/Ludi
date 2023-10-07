package com.mr3y.ludi.shared.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import okio.BufferedSink
import okio.BufferedSource
import okio.IOException

object FavouriteGamesSerializer : OkioSerializer<UserFavouriteGames> {
    override val defaultValue: UserFavouriteGames = UserFavouriteGames()

    override suspend fun readFrom(source: BufferedSource): UserFavouriteGames {
        try {
            return UserFavouriteGames.ADAPTER.decode(source)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read UserFavouriteGames proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserFavouriteGames, sink: BufferedSink) {
        UserFavouriteGames.ADAPTER.encode(sink, t)
    }
}
