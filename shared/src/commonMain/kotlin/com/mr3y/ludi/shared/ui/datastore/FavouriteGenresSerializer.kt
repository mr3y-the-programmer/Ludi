package com.mr3y.ludi.shared.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import okio.BufferedSink
import okio.BufferedSource
import okio.IOException

object FavouriteGenresSerializer : OkioSerializer<UserFavouriteGenres> {
    override val defaultValue: UserFavouriteGenres = UserFavouriteGenres()

    override suspend fun readFrom(source: BufferedSource): UserFavouriteGenres {
        try {
            return UserFavouriteGenres.ADAPTER.decode(source)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read UserFavouriteGenres proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserFavouriteGenres, sink: BufferedSink) {
        UserFavouriteGenres.ADAPTER.encode(sink, t)
    }
}
