package com.mr3y.ludi.shared.core.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import com.mr3y.ludi.shared.ArticleEntity
import com.mr3y.ludi.shared.core.database.LudiDatabase
import com.mr3y.ludi.shared.core.database.mapToArticleEntity
import com.mr3y.ludi.shared.core.database.toArticleEntity
import com.mr3y.ludi.shared.core.model.Article
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.di.DatabaseDispatcher
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

interface ArticleEntitiesDao {
    fun queryNewsArticles(searchQuery: String? = null): PagingSource<Int, ArticleEntity>

    fun queryReviewArticles(searchQuery: String? = null): PagingSource<Int, ArticleEntity>

    fun queryNewReleaseArticles(): PagingSource<Int, ArticleEntity>

    suspend fun updateDatabaseNewsArticles(articles: Set<NewsArticle>)

    suspend fun updateDatabaseReviewArticles(articles: Set<ReviewArticle>)

    suspend fun updateDatabaseNewReleaseArticles(articles: Set<NewReleaseArticle>)
}

@Inject
class DefaultArticleEntitiesDao(
    private val database: LudiDatabase,
    private val dispatcherWrapper: DatabaseDispatcher
) : ArticleEntitiesDao {
    override fun queryNewsArticles(searchQuery: String?): PagingSource<Int, ArticleEntity> {
        return QueryPagingSource(
            countQuery = if (!searchQuery.isNullOrBlank()) {
                database.articleSearchFTSQueries.countSearchResults(searchQuery.sanitize(), "news")
            } else {
                database.articleQueries.countArticles("news")
            },
            transacter = if (!searchQuery.isNullOrBlank()) database.articleSearchFTSQueries else database.articleQueries,
            context = dispatcherWrapper.dispatcher,
            queryProvider = { limit, offset ->
                if (!searchQuery.isNullOrBlank()) {
                    database.articleSearchFTSQueries.search(searchQuery.sanitize(), "news", limit, offset, ::mapToArticleEntity)
                } else {
                    database.articleQueries.queryArticles("news", limit, offset)
                }
            }
        )
    }

    override fun queryReviewArticles(searchQuery: String?): PagingSource<Int, ArticleEntity> {
        return QueryPagingSource(
            countQuery = if (!searchQuery.isNullOrBlank()) {
                database.articleSearchFTSQueries.countSearchResults(searchQuery.sanitize(), "reviews")
            } else {
                database.articleQueries.countArticles("reviews")
            },
            transacter = if (!searchQuery.isNullOrBlank()) database.articleSearchFTSQueries else database.articleQueries,
            context = dispatcherWrapper.dispatcher,
            queryProvider = { limit, offset ->
                if (!searchQuery.isNullOrBlank()) {
                    database.articleSearchFTSQueries.search(searchQuery.sanitize(), "reviews", limit, offset, ::mapToArticleEntity)
                } else {
                    database.articleQueries.queryArticles("reviews", limit, offset)
                }
            }
        )
    }

    override fun queryNewReleaseArticles(): PagingSource<Int, ArticleEntity> {
        return QueryPagingSource(
            countQuery = database.articleQueries.countArticles("new_releases"),
            transacter = database.articleQueries,
            context = dispatcherWrapper.dispatcher,
            queryProvider = { limit, offset ->
                database.articleQueries.queryArticles("new_releases", limit, offset)
            }
        )
    }

    override suspend fun updateDatabaseNewsArticles(articles: Set<NewsArticle>) {
        deleteAndInsertArticles(articles, "news", NewsArticle::toArticleEntity)
    }

    override suspend fun updateDatabaseReviewArticles(articles: Set<ReviewArticle>) {
        deleteAndInsertArticles(articles, "reviews", ReviewArticle::toArticleEntity)
    }

    override suspend fun updateDatabaseNewReleaseArticles(articles: Set<NewReleaseArticle>) {
        deleteAndInsertArticles(articles, "new_releases", NewReleaseArticle::toArticleEntity)
    }

    private suspend fun <T : Article> deleteAndInsertArticles(
        articles: Set<T>,
        type: String,
        mapper: (T) -> ArticleEntity
    ) = withContext(dispatcherWrapper.dispatcher) {
        database.articleQueries.transaction {
            database.articleQueries.deleteArticlesByType(type = type)
            articles.forEach { article ->
                database.articleQueries.insertArticle(mapper(article))
            }
        }
    }

    private fun String.sanitize() = "\"$this\""
}
