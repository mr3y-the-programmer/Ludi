package com.mr3y.ludi.shared.core.database.dao

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mr3y.ludi.shared.ArticleEntity
import com.mr3y.ludi.shared.core.database.LudiDatabase
import com.mr3y.ludi.shared.core.database.MarkupTextColumnAdapter
import com.mr3y.ludi.shared.core.database.TitleColumnAdapter
import com.mr3y.ludi.shared.core.database.ZonedDateTimeAdapter
import com.mr3y.ludi.shared.core.database.toNewReleaseArticle
import com.mr3y.ludi.shared.core.database.toNewsArticle
import com.mr3y.ludi.shared.core.database.toReviewArticle
import com.mr3y.ludi.shared.core.model.Article
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.core.network.datasources.internal.RSSFeedSamples
import com.mr3y.ludi.shared.di.DatabaseDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.doesNotContain
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

@Suppress("UNCHECKED_CAST")
class ArticleEntitiesDaoTest {

    private lateinit var database: LudiDatabase
    private lateinit var sut: DefaultArticleEntitiesDao

    private val testDispatcher = StandardTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        LudiDatabase.Schema.synchronous().create(driver)
        database = LudiDatabase(
            driver,
            ArticleEntity.Adapter(
                titleAdapter = TitleColumnAdapter,
                descriptionAdapter = MarkupTextColumnAdapter,
                sourceAdapter = EnumColumnAdapter(),
                contentAdapter = MarkupTextColumnAdapter,
                publicationDateAdapter = ZonedDateTimeAdapter
            )
        )

        sut = DefaultArticleEntitiesDao(database, DatabaseDispatcher(testDispatcher))
    }

    @Test
    fun `test news articles are saved & queried based on our requirements`() {
        testSavingAndQueryingFor(
            type = "news",
            samples = RSSFeedSamples.NewsFeed.articles as List<NewsArticle>,
            insertProvider = sut::updateDatabaseNewsArticles,
            mapper = ArticleEntity::toNewsArticle
        )
    }

    @Test
    fun `test reviews articles are saved & queried based on our requirements`() {
        testSavingAndQueryingFor(
            type = "reviews",
            samples = RSSFeedSamples.ReviewsFeed.articles as List<ReviewArticle>,
            insertProvider = sut::updateDatabaseReviewArticles,
            mapper = ArticleEntity::toReviewArticle
        )
    }

    @Test
    fun `test new releases articles are saved & queried based on our requirements`() {
        testSavingAndQueryingFor(
            type = "new_releases",
            samples = RSSFeedSamples.NewReleasesFeed.articles as List<NewReleaseArticle>,
            insertProvider = sut::updateDatabaseNewReleaseArticles,
            mapper = ArticleEntity::toNewReleaseArticle
        )
    }

    @After
    fun teardown() {
        testScope.launch {
            database.articleQueries.deleteArticlesByType("news")
            database.articleQueries.deleteArticlesByType("reviews")
            database.articleQueries.deleteArticlesByType("new_releases")
        }
    }

    private fun <T : Article> testSavingAndQueryingFor(
        type: String,
        samples: List<T>,
        insertProvider: suspend (Set<T>) -> Unit,
        mapper: (ArticleEntity) -> T
    ) = runTest(testDispatcher) {
        // Initially, articles table is empty.
        val initialData = database.articleQueries.queryArticles(type, limit = 100L, offset = 0L).executeAsList()
        expectThat(initialData).isEmpty()

        // when saving a new article into the database.
        insertProvider(setOf(samples.first()))

        // then our table should contain exactly 1 row
        val articles = database.articleQueries.queryArticles(type, limit = 100L, offset = 0L).executeAsList().map { mapper(it) }
        expectThat(articles).isEqualTo(listOf(samples.first()))

        // And new articles added after-then should replace what already exists in the table
        insertProvider(samples.drop(1).toSet())
        val newArticles = database.articleQueries.queryArticles(type, limit = 100L, offset = 0L).executeAsList().map { mapper(it) }
        expectThat(newArticles).containsExactlyInAnyOrder(samples.drop(1))
        expectThat(newArticles).doesNotContain(samples.first())
    }
}
