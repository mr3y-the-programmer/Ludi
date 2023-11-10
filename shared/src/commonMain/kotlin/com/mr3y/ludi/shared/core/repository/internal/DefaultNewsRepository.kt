package com.mr3y.ludi.shared.core.repository.internal

import androidx.paging.filter
import androidx.paging.map
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.mr3y.ludi.shared.ArticleEntity
import com.mr3y.ludi.shared.core.Logger
import com.mr3y.ludi.shared.core.database.dao.ArticleEntitiesDao
import com.mr3y.ludi.shared.core.database.toNewReleaseArticle
import com.mr3y.ludi.shared.core.database.toNewsArticle
import com.mr3y.ludi.shared.core.database.toReviewArticle
import com.mr3y.ludi.shared.core.model.Article
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.core.model.Source
import com.mr3y.ludi.shared.core.model.data
import com.mr3y.ludi.shared.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.shared.core.repository.NewsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import java.time.ZonedDateTime

@Suppress("UNCHECKED_CAST")
@Inject
class DefaultNewsRepository(
    private val rssFeedDataSource: RSSFeedDataSource,
    private val articleEntitiesDao: ArticleEntitiesDao,
    private val logger: Logger
) : NewsRepository {

    override fun queryLatestGamingNews(searchQuery: String?): Flow<PagingData<NewsArticle>> {
        return Pager(DefaultPagingConfig) {
            articleEntitiesDao.queryNewsArticles(searchQuery)
        }
            .flow
            .map { pagingData ->
                pagingData.map(ArticleEntity::toNewsArticle)
            }
    }

    override fun queryGamesNewReleases(): Flow<PagingData<NewReleaseArticle>> {
        return Pager(DefaultPagingConfig) {
            articleEntitiesDao.queryNewReleaseArticles()
        }
            .flow
            .map { pagingData ->
                pagingData
                    .map(ArticleEntity::toNewReleaseArticle)
                    .filter { article -> article.releaseDate.isAfter(ZonedDateTime.now()) }
            }
    }

    override fun queryGamesReviews(searchQuery: String?): Flow<PagingData<ReviewArticle>> {
        return Pager(DefaultPagingConfig) {
            articleEntitiesDao.queryReviewArticles(searchQuery)
        }
            .flow
            .map { pagingData ->
                pagingData.map(ArticleEntity::toReviewArticle)
            }
    }

    override suspend fun updateGamingNews(sources: Set<Source>, forceRefresh: Boolean): Boolean {
        var fetchedNetworkUpdates = false
        if (forceRefresh) {
            val networkArticlesResult = fetchAndAggregateFeedArticles(
                sources = sources,
                fetcher = { source -> rssFeedDataSource.fetchNewsFeed(source) }
            ) as Result<Set<NewsArticle>, Throwable>

            when (networkArticlesResult) {
                is Result.Success -> {
                    articleEntitiesDao.updateDatabaseNewsArticles(networkArticlesResult.data)
                    fetchedNetworkUpdates = true
                }
                else -> {}
            }
        }
        return fetchedNetworkUpdates
    }

    override suspend fun updateGamesNewReleases(sources: Set<Source>, forceRefresh: Boolean): Boolean {
        var fetchedNetworkUpdates = false
        if (forceRefresh) {
            val networkArticlesResult = fetchAndAggregateFeedArticles(
                sources = sources,
                fetcher = { source -> rssFeedDataSource.fetchNewReleasesFeed(source) }
            ) as Result<Set<NewReleaseArticle>, Throwable>

            when (networkArticlesResult) {
                is Result.Success -> {
                    articleEntitiesDao.updateDatabaseNewReleaseArticles(networkArticlesResult.data)
                    fetchedNetworkUpdates = true
                }
                else -> {}
            }
        }
        return fetchedNetworkUpdates
    }

    override suspend fun updateGamesReviews(sources: Set<Source>, forceRefresh: Boolean): Boolean {
        var fetchedNetworkUpdates = false
        if (forceRefresh) {
            val networkArticlesResult = fetchAndAggregateFeedArticles(
                sources = sources,
                fetcher = { source -> rssFeedDataSource.fetchReviewsFeed(source) }
            ) as Result<Set<ReviewArticle>, Throwable>

            when (networkArticlesResult) {
                is Result.Success -> {
                    articleEntitiesDao.updateDatabaseReviewArticles(networkArticlesResult.data)
                    fetchedNetworkUpdates = true
                }
                else -> {}
            }
        }
        return fetchedNetworkUpdates
    }

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

    companion object {
        private val DefaultPagingConfig = PagingConfig(pageSize = 10, initialLoadSize = 10)
    }
}
