package com.mr3y.ludi.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import java.io.InputStream
import java.io.OutputStream

object FavouriteGamesSerializer : Serializer<UserFavouriteGames> {
    override val defaultValue: UserFavouriteGames = UserFavouriteGames.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserFavouriteGames {
        try {
            return UserFavouriteGames.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read UserFavouriteGames proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserFavouriteGames, output: OutputStream) {
        t.writeTo(output)
    }
}
