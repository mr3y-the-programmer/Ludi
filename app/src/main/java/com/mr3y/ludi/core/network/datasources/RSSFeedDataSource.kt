package com.mr3y.ludi.core.network.datasources

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.Source
import com.prof.rssparser.Article

interface RSSFeedDataSource {

    suspend fun fetchNewsFeed(source: Source): Result<List<Article>, Throwable>?

    suspend fun fetchReviewsFeed(source: Source): Result<List<Article>, Throwable>?

    suspend fun fetchNewReleasesFeed(source: Source): Result<List<Article>, Throwable>?
}
