package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedDataSource
import com.mr3y.ludi.core.network.model.IGNArticle
import com.mr3y.ludi.core.network.model.toIGNArticle
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class IGNDataSource @Inject constructor(private val parser: Parser) : RSSNewsFeedDataSource<IGNArticle> {
    override suspend fun fetchNewsFeed(): Result<List<IGNArticle>, Throwable> {
        return try {
            parser.getChannel(RSSNewsFeedURL)
                .articles
                .map(Article::toIGNArticle)
                .let { Result.Success(it) }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Result.Error(e)
        }
    }

    companion object {
        const val RSSNewsFeedURL = "https://feeds.feedburner.com/ign/games-all"
    }
}
