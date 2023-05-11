package com.mr3y.ludi.core.network.datasources.internal

import co.touchlab.kermit.Logger
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSReviewsFeedDataSource
import com.mr3y.ludi.core.network.model.TechRadarArticle
import com.mr3y.ludi.core.network.model.toTechRadarArticle
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class TechRadarGamingDataSource @Inject constructor(
    private val parser: Parser,
    private val logger: Logger
) : RSSNewsFeedDataSource<TechRadarArticle>, RSSReviewsFeedDataSource<TechRadarArticle> {
    override suspend fun fetchNewsFeed(): Result<List<TechRadarArticle>, Throwable> {
        logger.d { "Fetching TechRadar News Feed" }
        return fetchFeedFromUrl(RSSNewsFeedURL)
    }

    override suspend fun fetchReviewsFeed(): Result<List<TechRadarArticle>, Throwable> {
        logger.d { "Fetching TechRadar Reviews Feed" }
        return fetchFeedFromUrl(RSSReviewsFeedURL)
    }

    private suspend fun fetchFeedFromUrl(url: String): Result<List<TechRadarArticle>, Throwable> {
        return try {
            parser.getChannel(url)
                .articles
                .map(Article::toTechRadarArticle)
                .let { Result.Success(it) }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            logger.e(e) { "Error occurred while trying to fetch Tech Radar feed from $url" }
            Result.Error(e)
        }
    }

    companion object {
        const val RSSNewsFeedURL = "https://www.techradar.com/rss/news/gaming"
        const val RSSReviewsFeedURL = "https://www.techradar.com/rss/reviews/gaming"
    }
}
