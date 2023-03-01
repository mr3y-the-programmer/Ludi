package com.mr3y.ludi.core.network.datasources

import com.mr3y.ludi.core.model.Result

fun interface RSSNewReleasesFeedProvider<T> {
    suspend fun fetchNewReleasesFeed(): Result<List<T>, Throwable>
}