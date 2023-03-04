package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.datasources.RSSNewReleasesFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSReviewsFeedDataSource
import com.mr3y.ludi.core.network.model.GameSpotArticle
import com.mr3y.ludi.core.network.model.toGameSpotArticle
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class GameSpotDataSource @Inject constructor(
    private val parser: Parser
) : RSSNewsFeedDataSource<GameSpotArticle>, RSSReviewsFeedDataSource<GameSpotArticle>, RSSNewReleasesFeedDataSource<GameSpotArticle> {
    override suspend fun fetchNewReleasesFeed(): Result<List<GameSpotArticle>, Throwable> {
        return fetchFeedFromUrl(RSSNewReleasesFeedURL)
    }

    override suspend fun fetchNewsFeed(): Result<List<GameSpotArticle>, Throwable> {
        return fetchFeedFromUrl(RSSNewsFeedURL)
    }

    override suspend fun fetchReviewsFeed(): Result<List<GameSpotArticle>, Throwable> {
        return fetchFeedFromUrl(RSSReviewsFeedURL)
    }

    private suspend fun fetchFeedFromUrl(url: String): Result<List<GameSpotArticle>, Throwable> {
        return try {
            parser.getChannel(url)
                .articles
                .map(Article::toGameSpotArticle)
                .let { Result.Success(it) }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Result.Error(e)
        }
    }

    companion object {
        const val BaseRSSFeedURL = "https://www.gamespot.com/feeds"
        const val RSSNewsFeedURL = "$BaseRSSFeedURL/game-news"
        const val RSSNewReleasesFeedURL = "$BaseRSSFeedURL/new-games"
        const val RSSReviewsFeedURL = "$BaseRSSFeedURL/reviews"
    }
}