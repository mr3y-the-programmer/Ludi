package com.mr3y.ludi.shared.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

actual object FollowedNewsDataSourceSerializer : Serializer<FollowedNewsDataSources> {
    override val defaultValue: FollowedNewsDataSources = FollowedNewsDataSources()

    override suspend fun readFrom(input: InputStream): FollowedNewsDataSources {
        try {
            return FollowedNewsDataSources.ADAPTER.decode(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read FollowedNewsDataSources proto.", exception)
        }
    }

    override suspend fun writeTo(t: FollowedNewsDataSources, output: OutputStream) {
        FollowedNewsDataSources.ADAPTER.encode(output, t)
    }
}