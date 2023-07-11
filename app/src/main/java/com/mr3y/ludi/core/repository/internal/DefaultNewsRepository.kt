package com.mr3y.ludi.core.repository.internal

import co.touchlab.kermit.Logger
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.data
import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.network.model.toNewReleaseArticle
import com.mr3y.ludi.core.network.model.toNewsArticle
import com.mr3y.ludi.core.network.model.toReviewArticle
import com.mr3y.ludi.core.repository.NewsRepository
import com.prof.rssparser.Article
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DefaultNewsRepository @Inject constructor(
    private val rssFeedDataSource: RSSFeedDataSource,
    private val logger: Logger
) : NewsRepository {

    override suspend fun getLatestGamingNews(sources: Set<Source>): Result<Set<NewsArticle>, Throwable> = fetchFeedArticlesAndTransform(
        sources = sources,
        fetcher = { source -> rssFeedDataSource.fetchNewsFeed(source) },
        transformer = { article, source -> article.toNewsArticle(source) }
    )

    override suspend fun getGamesNewReleases(sources: Set<Source>): Result<Set<NewReleaseArticle>, Throwable> = fetchFeedArticlesAndTransform(
        sources = sources,
        fetcher = { source -> rssFeedDataSource.fetchNewReleasesFeed(source) },
        transformer = { article, source -> article.toNewReleaseArticle(source) }
    )

    override suspend fun getGamesReviews(sources: Set<Source>): Result<Set<ReviewArticle>, Throwable> = fetchFeedArticlesAndTransform(
        sources = sources,
        fetcher = { source -> rssFeedDataSource.fetchReviewsFeed(source) },
        transformer = { article, source -> article.toReviewArticle(source) }
    )

    private suspend fun <T> fetchFeedArticlesAndTransform(
        sources: Set<Source>,
        fetcher: suspend (Source) -> Result<List<Article>, Throwable>?,
        transformer: (Article, Source) -> T?
    ): Result<Set<T>, Throwable> = coroutineScope {
        val aggregatedErrors = mutableListOf<Result.Error>()
        val aggregatedSuccessResponses = sources.map { source ->
            async {
                fetcher(source)?.let { result ->
                    result.data?.mapNotNull { article -> transformer(article, source) }?.let { Result.Success(it) } ?: result as Result.Error
                }
            }
        }.awaitAll().fold(Result.Success<Set<T>>(emptySet())) { acc, result ->
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
