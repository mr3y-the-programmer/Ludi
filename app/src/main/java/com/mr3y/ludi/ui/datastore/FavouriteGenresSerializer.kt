package com.mr3y.ludi.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.mr3y.ludi.UserFavouriteGenres
import java.io.InputStream
import java.io.OutputStream

object FavouriteGenresSerializer : Serializer<UserFavouriteGenres> {
    override val defaultValue: UserFavouriteGenres = UserFavouriteGenres.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserFavouriteGenres {
        try {
            return UserFavouriteGenres.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read UserFavouriteGenres proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserFavouriteGenres, output: OutputStream) {
        t.writeTo(output)
    }
}
