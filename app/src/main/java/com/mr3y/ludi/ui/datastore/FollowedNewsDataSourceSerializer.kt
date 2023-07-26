package com.mr3y.ludi.ui.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import java.io.InputStream
import java.io.OutputStream

object FollowedNewsDataSourceSerializer : Serializer<FollowedNewsDataSources> {
    override val defaultValue: FollowedNewsDataSources = FollowedNewsDataSources.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): FollowedNewsDataSources {
        try {
            return FollowedNewsDataSources.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read FollowedNewsDataSources proto.", exception)
        }
    }

    override suspend fun writeTo(t: FollowedNewsDataSources, output: OutputStream) {
        t.writeTo(output)
    }
}
