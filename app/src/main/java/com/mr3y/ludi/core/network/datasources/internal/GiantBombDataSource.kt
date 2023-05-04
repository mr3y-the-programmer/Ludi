package com.mr3y.ludi.core.network.datasources.internal

import co.touchlab.kermit.Logger
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.datasources.RSSNewReleasesFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSReviewsFeedDataSource
import com.mr3y.ludi.core.network.model.GiantBombArticle
import com.mr3y.ludi.core.network.model.toGiantBombArticle
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class GiantBombDataSource @Inject constructor(
    private val parser: Parser,
    private val logger: Logger
) : RSSNewsFeedDataSource<GiantBombArticle>, RSSNewReleasesFeedDataSource<GiantBombArticle>, RSSReviewsFeedDataSource<GiantBombArticle> {

    override suspend fun fetchNewsFeed(): Result<List<GiantBombArticle>, Throwable> {
        logger.d { "Fetching GiantBomb News Feed" }
        return fetchFeedFromUrl(RSSNewsFeedURL)
    }

    override suspend fun fetchNewReleasesFeed(): Result<List<GiantBombArticle>, Throwable> {
        logger.d { "Fetching GiantBomb New releases Feed" }
        return fetchFeedFromUrl(RSSNewReleasesFeedURL)
    }

    override suspend fun fetchReviewsFeed(): Result<List<GiantBombArticle>, Throwable> {
        logger.d { "Fetching GiantBomb Reviews Feed" }
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
            logger.e(e) { "Error occurred while trying to fetch GiantBomb feed from $url" }
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
