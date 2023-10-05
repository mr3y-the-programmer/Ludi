package com.mr3y.ludi.shared.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

actual object FavouriteGenresSerializer : Serializer<UserFavouriteGenres> {
    override val defaultValue: UserFavouriteGenres = UserFavouriteGenres()

    override suspend fun readFrom(input: InputStream): UserFavouriteGenres {
        try {
            return UserFavouriteGenres.ADAPTER.decode(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read UserFavouriteGenres proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserFavouriteGenres, output: OutputStream) {
        UserFavouriteGenres.ADAPTER.encode(output, t)
    }
}