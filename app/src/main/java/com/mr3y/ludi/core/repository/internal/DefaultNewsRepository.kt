package com.mr3y.ludi.core.repository.internal

import co.touchlab.kermit.Logger
import com.mr3y.ludi.core.model.Article
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.data
import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.repository.NewsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DefaultNewsRepository @Inject constructor(
    private val rssFeedDataSource: RSSFeedDataSource,
    private val logger: Logger
) : NewsRepository {

    override suspend fun getLatestGamingNews(sources: Set<Source>): Result<Set<NewsArticle>, Throwable> = fetchAndAggregateFeedArticles(
        sources = sources,
        fetcher = { source -> rssFeedDataSource.fetchNewsFeed(source) }
    ) as Result<Set<NewsArticle>, Throwable>

    override suspend fun getGamesNewReleases(sources: Set<Source>): Result<Set<NewReleaseArticle>, Throwable> = fetchAndAggregateFeedArticles(
        sources = sources,
        fetcher = { source -> rssFeedDataSource.fetchNewReleasesFeed(source) }
    ) as Result<Set<NewReleaseArticle>, Throwable>

    override suspend fun getGamesReviews(sources: Set<Source>): Result<Set<ReviewArticle>, Throwable> = fetchAndAggregateFeedArticles(
        sources = sources,
        fetcher = { source -> rssFeedDataSource.fetchReviewsFeed(source) }
    ) as Result<Set<ReviewArticle>, Throwable>

    private suspend fun fetchAndAggregateFeedArticles(
        sources: Set<Source>,
        fetcher: suspend (Source) -> Result<List<Article>, Throwable>?
    ): Result<Set<Article>, Throwable> = coroutineScope {
        val aggregatedErrors = mutableListOf<Result.Error>()
        val aggregatedSuccessResponses = sources.map { source ->
            async {
                fetcher(source)?.let { result ->
                    result.data?.let { Result.Success(it) } ?: result as Result.Error
                }
            }
        }.awaitAll().filterNotNull().fold(Result.Success<Set<Article>>(emptySet())) { acc, result ->
            when (result) {
                is Result.Success -> { acc.copy(data = acc.data + result.data) }
                is Result.Error -> {
                    logger.e(result.exception) {
                        "Exception occurred while trying to fetch rss feed, aggregated data before throwing exception $acc"
                    }
                    aggregatedErrors += result
                    return@fold acc
                }
                else -> acc
            }
        }
        when {
            aggregatedSuccessResponses.data.isEmpty() && aggregatedErrors.isNotEmpty() -> Result.Error()
            else -> aggregatedSuccessResponses
        }
    }
}
