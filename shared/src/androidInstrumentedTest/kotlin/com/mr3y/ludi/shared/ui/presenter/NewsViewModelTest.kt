package com.mr3y.ludi.shared.ui.presenter

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mr3y.ludi.shared.core.model.Source
import com.mr3y.ludi.shared.core.model.data
import com.mr3y.ludi.datastore.model.FollowedNewsDataSource
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.shared.ui.datastore.FollowedNewsDataSourceSerializer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isContainedIn
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class NewsViewModelTest {

    @get:Rule(order = 0)
    val tempFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
    private val testScope = TestScope(testDispatcher)

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources> = DataStoreFactory.create(
        storage = OkioStorage(fileSystem = FileSystem.SYSTEM, serializer = FollowedNewsDataSourceSerializer, producePath = { tempFolder.newFolder().toOkioPath().resolve("datastore").resolve("followed_news_sources.pb") }),
        corruptionHandler = null,
        migrations = emptyList(),
        scope = testScope
    )

    private lateinit var sut: NewsViewModel

    @Before
    fun setUp() {
        sut = NewsViewModel(
            newsRepository = FakeNewsRepository(),
            followedNewsDataSourcesStore = followedNewsDataSourcesStore
        )
        testScope.launch {
            followedNewsDataSourcesStore.updateData { it.copy(newsDataSource = emptyList()) }
        }
    }

    @Test
    fun userPreferresSpecificDataSources_ExpectSortedFeedResultsFromOnlyThoseDataSources() = runTest {
        backgroundScope.launch {
            sut.newsState.collect()
        }
        expectThat(sut.newsState.value).isEqualTo(NewsViewModel.InitialNewsState)

        followedNewsDataSourcesStore.updateData {
            it.copy(
                newsDataSource = listOf(
                    followedNewsDataSource("GiantBomb", 1293137, Source.GiantBomb),
                    followedNewsDataSource("GameSpot", 1592037, Source.GameSpot),
                    followedNewsDataSource("EuroGamer", 4263199, Source.EuroGamer)
                )
            )
        }

        advanceUntilIdle()

        sut.newsState.value.newsFeed.data?.let { articles ->
            with(sut) {
                expectThat(articles.sortByRecent()).isEqualTo(articles)
            }
            articles.forEach { article ->
                article.let {
                    expectThat(it.source).isContainedIn(setOf(Source.GameSpot, Source.GiantBomb, Source.EuroGamer))
                }
            }
        }
        sut.newsState.value.reviewsFeed.data?.let { articles ->
            with(sut) {
                expectThat(articles.sortByRecent()).isEqualTo(articles)
            }
            articles.forEach { article ->
                article.let {
                    expectThat(it.source).isContainedIn(setOf(Source.GameSpot, Source.GiantBomb, Source.EuroGamer))
                }
            }
        }
        sut.newsState.value.newReleasesFeed.data?.let { articles ->
            with(sut) {
                expectThat(articles.sortByRecent(desc = false)).isEqualTo(articles)
            }
            articles.forEach { article ->
                article.let {
                    expectThat(it.source).isContainedIn(setOf(Source.GameSpot, Source.GiantBomb))
                    expectThat(it.releaseDate).isGreaterThan(ZonedDateTime.now())
                }
            }
        }
    }

    @After
    fun teardown() {
        testScope.launch {
            followedNewsDataSourcesStore.updateData { it.copy(newsDataSource = emptyList()) }
        }
    }

    private fun followedNewsDataSource(name: String, drawableId: Int, type: Source): FollowedNewsDataSource {
        return FollowedNewsDataSource(name = name, drawableId = drawableId, type = type.name)
    }
}
