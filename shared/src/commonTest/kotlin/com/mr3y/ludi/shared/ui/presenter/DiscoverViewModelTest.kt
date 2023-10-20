package com.mr3y.ludi.shared.ui.presenter

import app.cash.paging.Pager
import com.mr3y.ludi.shared.MainDispatcherRule
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.shared.ui.presenter.model.Platform
import com.mr3y.ludi.shared.ui.presenter.model.Store
import com.mr3y.ludi.shared.ui.presenter.model.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher(TestCoroutineScheduler()))

    private lateinit var sut: DiscoverViewModel

    @Before
    fun setUp() {
        sut = DiscoverViewModel(
            pagingFactory = object : DiscoverPagingFactory {
                override val trendingGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val topRatedGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val favGenresBasedGamesPager: Pager<Int, Game>?
                    get() = TODO("Not yet implemented")
                override val multiplayerGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val freeGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val storyGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val boardGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val esportsGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val raceGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val puzzleGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")
                override val soundtrackGamesPager: Pager<Int, Game>
                    get() = TODO("Not yet implemented")

                override fun searchQueryBasedGamesPager(
                    searchQuery: String,
                    filters: DiscoverFiltersState
                ): Pager<Int, Game> {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    @Test
    fun `when filters, query or games are updated, state should be notified of updates`() = runTest {
        backgroundScope.launch(mainDispatcherRule.testDispatcher) {
            sut.discoverState.collect()
        }
        expectThat(sut.discoverState.value).isEqualTo(sut.Initial)

        sut.updateSearchQuery("stra")

        advanceUntilIdle()

        expectThat(sut.searchQuery.value).isEqualTo("stra")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SearchQueryBasedGames>()

        sut.addToSelectedPlatforms(Platform(id = 21, label = "Android"))

        expectThat(sut.discoverState.value.filtersState.selectedPlatforms).isEqualTo(setOf(Platform(id = 21, label = "Android")))
        expectThat(sut.searchQuery.value).isEqualTo("stra")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SearchQueryBasedGames>()

        sut.addToSelectedStores(Store(id = 4, label = "App store"))

        expectThat(sut.discoverState.value.filtersState.selectedStores).isEqualTo(setOf(Store(id = 4, label = "App store")))
        expectThat(sut.discoverState.value.filtersState.selectedPlatforms).isEqualTo(setOf(Platform(id = 21, label = "Android")))
        expectThat(sut.searchQuery.value).isEqualTo("stra")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SearchQueryBasedGames>()

        sut.addToSelectedTags(Tag(id = 3, label = "Adventure"))

        expectThat(sut.discoverState.value.filtersState.selectedTags).isEqualTo(setOf(Tag(id = 3, label = "Adventure")))
        expectThat(sut.discoverState.value.filtersState.selectedStores).isEqualTo(setOf(Store(id = 4, label = "App store")))
        expectThat(sut.discoverState.value.filtersState.selectedPlatforms).isEqualTo(setOf(Platform(id = 21, label = "Android")))
        expectThat(sut.searchQuery.value).isEqualTo("stra")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SearchQueryBasedGames>()

        sut.removeFromSelectedTags(Tag(id = 3, label = "Adventure"))
        sut.removeFromSelectedPlatforms(Platform(id = 21, label = "Android"))
        sut.removeFromSelectedStores(Store(id = 4, label = "App store"))
        sut.updateSearchQuery("")

        advanceUntilIdle()

        expectThat(sut.discoverState.value.filtersState.selectedTags).isEqualTo(emptySet())
        expectThat(sut.discoverState.value.filtersState.selectedStores).isEqualTo(emptySet())
        expectThat(sut.discoverState.value.filtersState.selectedPlatforms).isEqualTo(emptySet())
        expectThat(sut.searchQuery.value).isEqualTo("")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SuggestedGames>()
    }
}
