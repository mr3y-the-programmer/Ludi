package com.mr3y.ludi.core.repository.internal

import co.touchlab.kermit.Logger
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.toCoreErrorResult
import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSNewReleasesFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSReviewsFeedDataSource
import com.mr3y.ludi.core.network.datasources.internal.MMOGamesDataSource
import com.mr3y.ludi.core.network.model.MMOGamesArticle
import com.mr3y.ludi.core.network.model.RSSFeedArticle
import com.mr3y.ludi.core.repository.NewsRepository
import com.slack.eithernet.ApiResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DefaultNewsRepository @Inject constructor(
    private val rssDataSources: Map<Source, @JvmSuppressWildcards RSSFeedDataSource<RSSFeedArticle>>,
    private val mmoGamesDataSource: MMOGamesDataSource,
    private val logger: Logger
) : NewsRepository {

    override suspend fun getLatestGamingNews(sources: Set<Source>): Result<Set<NewsArticle>, Throwable> = coroutineScope {
        val aggregatedErrors = mutableListOf<Result.Error>()
        var aggregatedNews = rssDataSources.filter { it.key in sources && it.value is RSSNewsFeedDataSource<*> }.values.map {
            it as RSSNewsFeedDataSource<*>
            async {
                it.fetchNewsFeed()
            }
        }.awaitAll().fold(Result.Success<Set<NewsArticle>>(emptySet())) { acc, result ->
            when (result) {
                is Result.Success -> { acc.copy(data = acc.data + result.data.map { it.toNewsArticle()!! }) }
                is Result.Error -> {
                    logger.e(result.exception) { "aggregated data before throwing exception $acc" }
                    aggregatedErrors += result
                    return@fold acc
                }
            }
        }
        if (Source.MMOBomb in sources) {
            val mmoBombNewsResult = async {
                mmoGamesDataSource.getLatestNews("https://www.mmobomb.com/api1/latestnews")
            }
            mmoBombNewsResult.await().let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        aggregatedNews = aggregatedNews.copy(data = aggregatedNews.data + result.value.map(MMOGamesArticle::toNewsArticle))
                    }
                    is ApiResult.Failure -> {
                        logger.e(result.toCoreErrorResult().exception) { "$result" }
                        aggregatedErrors += result.toCoreErrorResult()
                    }
                }
            }
        }
        when {
            aggregatedNews.data.isEmpty() && aggregatedErrors.isNotEmpty() -> Result.Error()
            else -> aggregatedNews
        }
    }

    override suspend fun getGamesNewReleases(sources: Set<Source>): Result<Set<NewReleaseArticle>, Throwable> = coroutineScope {
        val aggregatedErrors = mutableListOf<Result.Error>()
        val aggregatedNewReleases = rssDataSources.filter { it.key in sources && it.value is RSSNewReleasesFeedDataSource<*> }.values.map {
            it as RSSNewReleasesFeedDataSource<*>
            async {
                it.fetchNewReleasesFeed()
            }
        }.awaitAll().fold(Result.Success<Set<NewReleaseArticle>>(emptySet())) { acc, result ->
            when (result) {
                is Result.Success -> { acc.copy(data = acc.data + result.data.map { it.toNewReleaseArticle()!! }) }
                is Result.Error -> {
                    logger.e(result.exception) { "aggregated data before throwing exception $acc" }
                    aggregatedErrors += result
                    return@fold acc
                }
            }
        }
        when {
            aggregatedNewReleases.data.isEmpty() && aggregatedErrors.isNotEmpty() -> Result.Error()
            else -> aggregatedNewReleases
        }
    }

    override suspend fun getGamesReviews(sources: Set<Source>): Result<Set<ReviewArticle>, Throwable> = coroutineScope {
        val aggregatedErrors = mutableListOf<Result.Error>()
        val aggregatedReviews = rssDataSources.filter { it.key in sources && it.value is RSSReviewsFeedDataSource<*> }.values.map {
            it as RSSReviewsFeedDataSource<*>
            async {
                it.fetchReviewsFeed()
            }
        }.awaitAll().fold(Result.Success<Set<ReviewArticle>>(emptySet())) { acc, result ->
            when (result) {
                is Result.Success -> { acc.copy(data = acc.data + result.data.map { it.toReviewArticle()!! }) }
                is Result.Error -> {
                    logger.e(result.exception) { "aggregated data before throwing exception $acc" }
                    aggregatedErrors += result
                    return@fold acc
                }
            }
        }
        when {
            aggregatedReviews.data.isEmpty() && aggregatedErrors.isNotEmpty() -> Result.Error()
            else -> aggregatedReviews
        }
    }
}
