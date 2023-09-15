package com.mr3y.ludi.core.repository.internal

import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.network.fixtures.FakeRSSFeedDataSource
import com.mr3y.ludi.core.network.fixtures.Feed
import com.mr3y.ludi.core.network.fixtures.PrintlnLogWriter
import com.mr3y.ludi.core.network.fixtures.logger
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@Suppress("UNCHECKED_CAST")
class DefaultNewsRepositoryTest {

    @Test
    fun whenGettingNewsSuccessfullyFromChosenDataSources_DataIsAggregated() = runTest {
        // when trying to get the latest available news
        val result = sut.getLatestGamingNews(setOf(Source.GameSpot, Source.GiantBomb, Source.IGN, Source.TechRadar))

        // then verify we get the data aggregated
        val expectedNews = setOf(
            fakeRssFeedDataSource.cache[Feed.News]!!,
            fakeRssFeedDataSource.cache[Feed.News]!!,
            fakeRssFeedDataSource.cache[Feed.News]!!,
            fakeRssFeedDataSource.cache[Feed.News]!!
        ).flatten().toSet() as Set<NewsArticle>

        expectThat(result).isA<Result.Success<List<NewsArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expectedNews)
        expectThat(logWriter.logs.size).isEqualTo(0)
    }

    @Test
    fun whenGettingPartialErrorsAndSomeSuccessfulResponses_SuccessfulResponsesAreReturnedAndErrorIsLogged() = runTest {
        // given some successful responses & a failure in specific datasource/s
        fakeRssFeedDataSource.simulateFailureOnDataSource(Source.TechRadar)

        // when trying to get the latest available news
        val result = sut.getLatestGamingNews(setOf(Source.GameSpot, Source.GiantBomb, Source.TechRadar))

        // then verify we get the aggregated successful responses & the error is logged
        val expectedNews = fakeRssFeedDataSource.cache[Feed.News]!!
            .plus(fakeRssFeedDataSource.cache[Feed.News]!!)
            .toSet() as Set<NewsArticle>

        expectThat(result).isA<Result.Success<List<NewsArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expectedNews)
        expectThat(logWriter.logs.size).isEqualTo(1)
    }

    @Test
    fun whenGettingAllErrorsAndNoSuccessfulResponses_ResultErrorIsReturned() = runTest {
        // given a failure in all data source and no successful responses
        fakeRssFeedDataSource.simulateFailure()

        // when trying to get the latest available news
        val result = sut.getLatestGamingNews(setOf(Source.GameSpot, Source.GiantBomb, Source.IGN))

        // then verify we get an error result
        expectThat(result).isA<Result.Error>()
        expectThat(logWriter.logs.size).isEqualTo(3)
    }

    @Test
    fun whenGettingReviewsSuccessfullyFromChosenDataSources_DataIsAggregated() = runTest {
        val result = sut.getGamesReviews(setOf(Source.GameSpot, Source.GiantBomb, Source.TechRadar, Source.IGN))

        val expected = setOf(
            fakeRssFeedDataSource.cache[Feed.Reviews]!!,
            fakeRssFeedDataSource.cache[Feed.Reviews]!!,
            fakeRssFeedDataSource.cache[Feed.Reviews]!!,
            fakeRssFeedDataSource.cache[Feed.Reviews]!!
        ).flatten().toSet() as Set<ReviewArticle>

        expectThat(result).isA<Result.Success<List<ReviewArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expected)
        expectThat(logWriter.logs.size).isEqualTo(0)
    }

    @Test
    fun whenGettingPartialErrorsAndSomeSuccessfulReviews_responsesAreReturned() = runTest {
        fakeRssFeedDataSource.simulateFailureOnDataSource(Source.IGN)

        val result = sut.getGamesReviews(setOf(Source.GameSpot, Source.TechRadar, Source.IGN))

        val expected = fakeRssFeedDataSource.cache[Feed.Reviews]!!
            .plus(fakeRssFeedDataSource.cache[Feed.Reviews]!!)
            .toSet() as Set<ReviewArticle>

        expectThat(result).isA<Result.Success<List<ReviewArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expected)
        expectThat(logWriter.logs.size).isEqualTo(1)
    }

    @Test
    fun whenGettingAllErrorsAndNoSuccessfulReviews_ResultErrorIsReturned() = runTest {
        fakeRssFeedDataSource.simulateFailure()

        val result = sut.getGamesReviews(setOf(Source.GameSpot, Source.GiantBomb))

        expectThat(result).isA<Result.Error>()
        expectThat(logWriter.logs.size).isEqualTo(2)
    }

    @Test
    fun whenGettingNewReleasesSuccessfullyFromChosenDataSources_DataIsAggregated() = runTest {
        val result = sut.getGamesNewReleases(setOf(Source.GameSpot, Source.GiantBomb, Source.TechRadar))

        val expected = setOf(
            fakeRssFeedDataSource.cache[Feed.NewReleases]!!,
            fakeRssFeedDataSource.cache[Feed.NewReleases]!!,
            fakeRssFeedDataSource.cache[Feed.NewReleases]!!
        ).flatten().toSet() as Set<NewReleaseArticle>

        expectThat(result).isA<Result.Success<List<NewReleaseArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expected)
        expectThat(logWriter.logs.size).isEqualTo(0)
    }

    @Test
    fun whenGettingPartialErrorsAndSomeSuccessfulNewReleases_responsesAreReturned() = runTest {
        fakeRssFeedDataSource.simulateFailureOnDataSource(Source.GiantBomb)

        val result = sut.getGamesNewReleases(setOf(Source.GameSpot, Source.GiantBomb))

        val expected = fakeRssFeedDataSource.cache[Feed.NewReleases]!!.toSet() as Set<NewReleaseArticle>

        expectThat(result).isA<Result.Success<List<NewReleaseArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expected)
        expectThat(logWriter.logs.size).isEqualTo(1)
    }

    @Test
    fun whenGettingAllErrorsAndNoSuccessfulNewReleases_ResultErrorIsReturned() = runTest {
        fakeRssFeedDataSource.simulateFailure()

        val result = sut.getGamesNewReleases(setOf(Source.GameSpot, Source.GiantBomb))

        expectThat(result).isA<Result.Error>()
        expectThat(logWriter.logs.size).isEqualTo(2)
    }

    @After
    fun teardown() {
        fakeRssFeedDataSource.reset()
        logWriter.reset()
    }

    companion object {

        private lateinit var sut: DefaultNewsRepository
        private val fakeRssFeedDataSource = FakeRSSFeedDataSource()
        private val logWriter = PrintlnLogWriter()

        @JvmStatic
        @BeforeClass
        fun setUp() {
            sut = DefaultNewsRepository(fakeRssFeedDataSource, logger(logWriter = logWriter))
        }
    }
}
