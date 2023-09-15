package com.mr3y.ludi.core.network.datasources.internal

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.mr3y.ludi.core.FakeCrashReporting
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.network.fixtures.TestLogger
import com.mr3y.ludi.core.network.rssparser.FakeRSSParser
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

@Suppress("UNCHECKED_CAST")
@RunWith(TestParameterInjector::class)
class DefaultRSSFeedDataSourceTest {

    @Test
    fun `when fetching RSS News Feed Succeed expect list Of Articles`(@TestParameter source: Source) = runTest {
        val url = DefaultRSSFeedDataSource.supportedNewsFeedDataSources[source] ?: run {
            expectThat(sut.fetchNewsFeed(source)).isNull()
            return@runTest
        }
        // given a mocked request with sample data
        rssParser.setNewsArticlesAtUrl(url) {
            RSSFeedSamples.NewsFeed.articles as List<NewsArticle>
        }

        // when trying to fetch rss feed
        val result = sut.fetchNewsFeed(source)

        // then assert we got the sample data parsed & transformed to our typed model
        expectThat(result).isA<Result.Success<List<NewsArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(RSSFeedSamples.NewsFeed.articles as List<NewsArticle>)
    }

    @Test
    fun `when fetching RSS Reviews Feed Succeed expect list Of Articles`(@TestParameter source: Source) = runTest {
        val url = DefaultRSSFeedDataSource.supportedReviewsFeedDataSources[source] ?: run {
            expectThat(sut.fetchReviewsFeed(source)).isNull()
            return@runTest
        }
        // given a mocked request with sample data
        rssParser.setReviewArticlesAtUrl(url) {
            RSSFeedSamples.ReviewsFeed.articles as List<ReviewArticle>
        }

        // when trying to fetch rss feed
        val result = sut.fetchReviewsFeed(source)

        // then assert we got the sample data parsed & transformed to our typed model
        expectThat(result).isA<Result.Success<List<ReviewArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(RSSFeedSamples.ReviewsFeed.articles as List<ReviewArticle>)
    }

    @Test
    fun `when fetching RSS New Releases Feed Succeed expect list Of Articles`(@TestParameter source: Source) = runTest {
        val url = DefaultRSSFeedDataSource.supportedNewReleasesFeedDataSources[source] ?: run {
            expectThat(sut.fetchNewReleasesFeed(source)).isNull()
            return@runTest
        }
        // given a mocked request with sample data
        rssParser.setNewReleaseArticlesAtUrl(url) {
            RSSFeedSamples.NewReleasesFeed.articles as List<NewReleaseArticle>
        }

        // when trying to fetch rss feed
        val result = sut.fetchNewReleasesFeed(source)

        // then assert we got the sample data parsed & transformed to our typed model
        expectThat(result).isA<Result.Success<List<NewReleaseArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(RSSFeedSamples.NewReleasesFeed.articles as List<NewReleaseArticle>)
    }

    @Test
    fun `when fetching RSS Feed fails expect Result#Error`(@TestParameter source: Source) = runTest {
        // Assuming there is a failure while trying to fetch rss feed
        rssParser.simulateFailure()

        // when trying to fetch rss feed
        val result1 = sut.fetchNewsFeed(source)
        val result2 = sut.fetchReviewsFeed(source)
        val result3 = sut.fetchNewReleasesFeed(source)

        // then assert the exception is caught and wrapped into a typed Result.Error
        if (DefaultRSSFeedDataSource.supportedNewsFeedDataSources[source] == null) {
            expectThat(result1).isNull()
        } else {
            expectThat(result1).isA<Result.Error>()
        }
        if (DefaultRSSFeedDataSource.supportedReviewsFeedDataSources[source] == null) {
            expectThat(result2).isNull()
        } else {
            expectThat(result2).isA<Result.Error>()
        }
        if (DefaultRSSFeedDataSource.supportedNewReleasesFeedDataSources[source] == null) {
            expectThat(result3).isNull()
        } else {
            expectThat(result3).isA<Result.Error>()
        }
    }

    @After
    fun cleanup() {
        rssParser.reset()
    }

    companion object {

        private lateinit var sut: DefaultRSSFeedDataSource
        private val rssParser = FakeRSSParser()

        @JvmStatic
        @BeforeClass
        fun setUp() {
            sut = DefaultRSSFeedDataSource(rssParser, TestLogger(), FakeCrashReporting())
        }
    }
}
