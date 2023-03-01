package com.mr3y.ludi.core.network.datasources

import com.mr3y.ludi.core.model.Result

fun interface RSSNewsFeedProvider<T> {
    suspend fun fetchNewsFeed(): Result<List<T>, Throwable>
}