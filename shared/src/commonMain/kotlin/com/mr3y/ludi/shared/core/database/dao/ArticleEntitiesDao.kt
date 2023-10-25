package com.mr3y.ludi.shared.core.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import com.mr3y.ludi.shared.ArticleEntity
import com.mr3y.ludi.shared.core.database.LudiDatabase
import com.mr3y.ludi.shared.core.database.toArticleEntity
import com.mr3y.ludi.shared.core.model.Article
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.di.DatabaseDispatcher
import com.mr3y.ludi.shared.di.annotations.Singleton
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
@Singleton
class ArticleEntitiesDao(
    private val database: LudiDatabase,
    private val dispatcherWrapper: DatabaseDispatcher
) {
    fun queryNewsArticles(): PagingSource<Int, ArticleEntity> {
        return QueryPagingSource(
            countQuery = database.articleQueries.countArticles("news"),
            transacter = database.articleQueries,
            context = dispatcherWrapper.dispatcher,
            queryProvider = { limit, offset ->
                database.articleQueries.queryArticles("news", limit, offset)
            }
        )
    }

    fun queryReviewArticles(): PagingSource<Int, ArticleEntity> {
        return QueryPagingSource(
            countQuery = database.articleQueries.countArticles("reviews"),
            transacter = database.articleQueries,
            context = dispatcherWrapper.dispatcher,
            queryProvider = { limit, offset ->
                database.articleQueries.queryArticles("reviews", limit, offset)
            }
        )
    }

    fun queryNewReleaseArticles(): PagingSource<Int, ArticleEntity> {
        return QueryPagingSource(
            countQuery = database.articleQueries.countArticles("new_releases"),
            transacter = database.articleQueries,
            context = dispatcherWrapper.dispatcher,
            queryProvider = { limit, offset ->
                database.articleQueries.queryArticles("new_releases", limit, offset)
            }
        )
    }

    suspend fun updateDatabaseNewsArticles(articles: Set<NewsArticle>) {
        deleteAndInsertArticles(articles, "news", NewsArticle::toArticleEntity)
    }

    suspend fun updateDatabaseReviewArticles(articles: Set<ReviewArticle>) {
        deleteAndInsertArticles(articles, "reviews", ReviewArticle::toArticleEntity)
    }

    suspend fun updateDatabaseNewReleaseArticles(articles: Set<NewReleaseArticle>) {
        deleteAndInsertArticles(articles, "new_releases", NewReleaseArticle::toArticleEntity)
    }

    private suspend fun <T: Article> deleteAndInsertArticles(
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
}
