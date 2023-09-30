package com.mr3y.ludi.shared.core.network.datasources.internal

import com.mr3y.ludi.shared.core.CrashReporting
import com.mr3y.ludi.shared.core.Logger
import com.mr3y.ludi.shared.core.model.Article
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.core.model.Source
import com.mr3y.ludi.shared.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.shared.core.network.rssparser.Parser
import kotlinx.coroutines.CancellationException
import me.tatarka.inject.annotations.Inject

@Suppress("UNCHECKED_CAST")
@Inject
class DefaultRSSFeedDataSource(
    private val parser: Parser,
    private val logger: Logger,
    private val crashReporting: CrashReporting
) : RSSFeedDataSource {

    override suspend fun fetchNewsFeed(source: Source): Result<List<NewsArticle>, Throwable>? {
        logger.d { "Fetching ${source.name} News Feed" }
        val url = supportedNewsFeedDataSources.entries.firstOrNull { it.key == source }?.value ?: return null
        return fetchFeedFromUrl(url, source, Type.News) as Result<List<NewsArticle>, Throwable>
    }

    override suspend fun fetchReviewsFeed(source: Source): Result<List<ReviewArticle>, Throwable>? {
        logger.d { "Fetching ${source.name} Reviews Feed" }
        val url = supportedReviewsFeedDataSources.entries.firstOrNull { it.key == source }?.value ?: return null
        return fetchFeedFromUrl(url, source, Type.Reviews) as Result<List<ReviewArticle>, Throwable>
    }

    override suspend fun fetchNewReleasesFeed(source: Source): Result<List<NewReleaseArticle>, Throwable>? {
        logger.d { "Fetching ${source.name} New releases Feed" }
        val url = supportedNewReleasesFeedDataSources.entries.firstOrNull { it.key == source }?.value ?: return null
        return fetchFeedFromUrl(url, source, Type.NewReleases) as Result<List<NewReleaseArticle>, Throwable>
    }

    private suspend fun fetchFeedFromUrl(url: String, source: Source, type: Type): Result<List<Article>, Throwable> {
        return try {
            when (type) {
                Type.News -> parser.parseNewsArticlesAtUrl(url, source)
                Type.Reviews -> parser.parseReviewArticlesAtUrl(url, source)
                Type.NewReleases -> parser.parseNewReleaseArticlesAtUrl(url, source)
            }.let {
                Result.Success(it.filterNotNull())
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                logger.d(e) { "Cancelled fetching rss feed from $url Coroutine!" }
                throw e
            }
            val errorMessage = "Error occurred while trying to fetch rss feed from $url"
            logger.e(e) { errorMessage }
            crashReporting.recordException(e, errorMessage)
            Result.Error(e)
        }
    }

    private enum class Type {
        News,
        Reviews,
        NewReleases
    }

    companion object {
        val supportedNewsFeedDataSources = mapOf(
            Source.GiantBomb to "https://www.giantbomb.com/feeds/news",
            Source.GameSpot to "https://www.gamespot.com/feeds/game-news",
            Source.IGN to "https://feeds.feedburner.com/ign/games-all",
            Source.TechRadar to "https://www.techradar.com/rss/news/gaming",
            Source.PCGamesN to "https://www.pcgamesn.com/mainrss.xml",
            Source.RockPaperShotgun to "https://www.rockpapershotgun.com/feed/news",
            Source.PCInvasion to "https://www.pcinvasion.com/news/feed/",
            Source.GloriousGaming to "https://www.gloriousgaming.com/blogs/gaming-news.atom",
            Source.EuroGamer to "https://www.eurogamer.net/feed/news",
            Source.VG247 to "https://www.vg247.com/feed/news",
            Source.TheGamer to "https://www.thegamer.com/feed/category/game-news/",
            Source.GameRant to "https://gamerant.com/feed/category/gaming/",
            Source.VentureBeat to "https://venturebeat.com/category/pc-gaming/feed/",
            Source.PCGamer to "https://www.pcgamer.com/rss/",
            Source.Polygon to "https://www.polygon.com/rss/index.xml"
        )
        val supportedReviewsFeedDataSources = mapOf(
            Source.GiantBomb to "https://www.giantbomb.com/feeds/reviews",
            Source.GameSpot to "https://www.gamespot.com/feeds/reviews",
            Source.TechRadar to "https://www.techradar.com/rss/reviews/gaming",
            Source.RockPaperShotgun to "https://www.rockpapershotgun.com/feed/reviews",
            Source.EuroGamer to "https://www.eurogamer.net/feed/reviews",
            Source.VG247 to "https://www.vg247.com/feed/reviews",
            Source.TheGamer to "https://www.thegamer.com/feed/category/game-reviews/",
            Source.GameRant to "https://gamerant.com/feed/category/game-reviews/",
            Source.BrutalGamer to "https://brutalgamer.com/category/reviews/pc-reviews/feed/"
        )
        val supportedNewReleasesFeedDataSources = mapOf(
            Source.GiantBomb to "https://www.giantbomb.com/feeds/new_releases",
            Source.GameSpot to "https://www.gamespot.com/feeds/new-games"
        )
    }
}
