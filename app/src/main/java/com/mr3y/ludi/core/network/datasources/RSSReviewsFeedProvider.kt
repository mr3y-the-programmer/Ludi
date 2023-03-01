package com.mr3y.ludi.core.network.datasources

import com.mr3y.ludi.core.model.Result

fun interface RSSReviewsFeedProvider<T> {
    suspend fun fetchReviewsFeed(): Result<List<T>, Throwable>
}