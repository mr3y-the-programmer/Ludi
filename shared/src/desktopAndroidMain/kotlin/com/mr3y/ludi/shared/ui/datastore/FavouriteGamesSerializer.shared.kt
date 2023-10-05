package com.mr3y.ludi.shared.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

actual object FavouriteGamesSerializer : Serializer<UserFavouriteGames> {
    override val defaultValue: UserFavouriteGames = UserFavouriteGames()

    override suspend fun readFrom(input: InputStream): UserFavouriteGames {
        try {
            return UserFavouriteGames.ADAPTER.decode(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read UserFavouriteGames proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserFavouriteGames, output: OutputStream) {
        UserFavouriteGames.ADAPTER.encode(output, t)
    }
}