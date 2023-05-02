package com.mr3y.ludi.core.network.datasources

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.model.RSSFeedArticle

fun interface RSSReviewsFeedDataSource<T : RSSFeedArticle> : RSSFeedDataSource<RSSFeedArticle> {
    suspend fun fetchReviewsFeed(): Result<List<T>, Throwable>
}
