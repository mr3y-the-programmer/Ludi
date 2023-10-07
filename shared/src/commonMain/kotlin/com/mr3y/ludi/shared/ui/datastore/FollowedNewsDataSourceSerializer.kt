package com.mr3y.ludi.shared.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import okio.BufferedSink
import okio.BufferedSource
import okio.IOException

object FollowedNewsDataSourceSerializer : OkioSerializer<FollowedNewsDataSources> {
    override val defaultValue: FollowedNewsDataSources = FollowedNewsDataSources()

    override suspend fun readFrom(source: BufferedSource): FollowedNewsDataSources {
        try {
            return FollowedNewsDataSources.ADAPTER.decode(source)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read FollowedNewsDataSources proto.", exception)
        }
    }

    override suspend fun writeTo(t: FollowedNewsDataSources, sink: BufferedSink) {
        FollowedNewsDataSources.ADAPTER.encode(sink, t)
    }
}
