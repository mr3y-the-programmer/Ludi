package com.mr3y.ludi.core.network.datasources

import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source

interface RSSFeedDataSource {

    suspend fun fetchNewsFeed(source: Source): Result<List<NewsArticle>, Throwable>?

    suspend fun fetchReviewsFeed(source: Source): Result<List<ReviewArticle>, Throwable>?

    suspend fun fetchNewReleasesFeed(source: Source): Result<List<NewReleaseArticle>, Throwable>?
}
