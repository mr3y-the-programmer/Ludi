package com.mr3y.ludi.core.network.datasources.internal

import co.touchlab.kermit.Logger
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class DefaultRSSFeedDataSource @Inject constructor(
    private val parser: Parser,
    private val logger: Logger
) : RSSFeedDataSource {

    override suspend fun fetchNewsFeed(source: Source): Result<List<Article>, Throwable>? {
        logger.d { "Fetching ${source.name} News Feed" }
        val url = supportedNewsFeedDataSources.entries.firstOrNull { it.key == source }?.value ?: return null
        return fetchFeedFromUrl(url)
    }

    override suspend fun fetchReviewsFeed(source: Source): Result<List<Article>, Throwable>? {
        logger.d { "Fetching ${source.name} Reviews Feed" }
        val url = supportedReviewsFeedDataSources.entries.firstOrNull { it.key == source }?.value ?: return null
        return fetchFeedFromUrl(url)
    }

    override suspend fun fetchNewReleasesFeed(source: Source): Result<List<Article>, Throwable>? {
        logger.d { "Fetching ${source.name} New releases Feed" }
        val url = supportedNewReleasesFeedDataSources.entries.firstOrNull { it.key == source }?.value ?: return null
        return fetchFeedFromUrl(url)
    }

    private suspend fun fetchFeedFromUrl(url: String): Result<List<Article>, Throwable> {
        return try {
            parser.getChannel(url)
                .articles
                .let { Result.Success(it) }
        } catch (e: Exception) {
            if (e is CancellationException) {
                logger.d(e) { "Cancelled fetching rss feed from $url Coroutine!" }
                throw e
            }
            logger.e(e) { "Error occurred while trying to fetch rss feed from $url" }
            Result.Error(e)
        }
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
