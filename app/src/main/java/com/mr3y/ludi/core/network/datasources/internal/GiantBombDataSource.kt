package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.datasources.RSSNewReleasesFeedProvider
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedProvider
import com.mr3y.ludi.core.network.datasources.RSSReviewsFeedProvider
import com.mr3y.ludi.core.network.model.GiantBombArticle
import com.mr3y.ludi.core.network.model.toGiantBombArticle
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class GiantBombDataSource @Inject constructor(
  private val parser: Parser,
) : RSSNewsFeedProvider<GiantBombArticle>, RSSNewReleasesFeedProvider<GiantBombArticle>, RSSReviewsFeedProvider<GiantBombArticle> {

    override suspend fun fetchNewsFeed(): Result<List<GiantBombArticle>, Throwable> {
        return fetchFeedFromUrl(RSSNewsFeedURL)
    }

    override suspend fun fetchNewReleasesFeed(): Result<List<GiantBombArticle>, Throwable> {
        return fetchFeedFromUrl(RSSNewReleasesFeedURL)
    }

    override suspend fun fetchReviewsFeed(): Result<List<GiantBombArticle>, Throwable> {
        return fetchFeedFromUrl(RSSReviewsFeedURL)
    }

    private suspend fun fetchFeedFromUrl(url: String): Result<List<GiantBombArticle>, Throwable> {
        return try {
            parser.getChannel(url)
                .articles
                .map(Article::toGiantBombArticle)
                .let { Result.Success(it) }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Result.Error(e)
        }
    }

    companion object {
        const val BaseRSSFeedURL = "https://www.giantbomb.com/feeds"
        const val RSSNewsFeedURL = "$BaseRSSFeedURL/news"
        const val RSSNewReleasesFeedURL = "$BaseRSSFeedURL/new_releases"
        const val RSSReviewsFeedURL = "$BaseRSSFeedURL/reviews"
    }
}