package com.mr3y.ludi.ui.presenter

import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.repository.fixtures.FakeGamesRepository
import com.mr3y.ludi.shared.MainDispatcherRule
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.Tag
import com.mr3y.ludi.ui.presenter.model.Platform
import com.mr3y.ludi.ui.presenter.model.Store
import com.mr3y.ludi.ui.presenter.usecases.FakeSuggestedGamesUseCase
import com.mr3y.ludi.ui.presenter.usecases.GetSearchQueryBasedGamesUseCaseImpl
import com.mr3y.ludi.ui.presenter.usecases.utils.groupByGenre
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
            getSuggestedGamesUseCase = FakeSuggestedGamesUseCase(FakeGamesRepository()),
            searchQueryBasedGamesUseCase = GetSearchQueryBasedGamesUseCaseImpl(FakeGamesRepository())
        )
    }

    @Test
    fun `when filters, query or games are updated, state should be notified of updates`() = runTest {
        backgroundScope.launch(mainDispatcherRule.testDispatcher) {
            sut.discoverState.collect()
        }
        expectThat(sut.discoverState.value).isEqualTo(DiscoverViewModel.Initial)

        sut.updateSearchQuery("stra")

        advanceUntilIdle()

        expectThat(sut.discoverState.value.searchQuery).isEqualTo("stra")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SearchQueryBasedGames>()

        sut.addToSelectedPlatforms(Platform(id = 21, label = "Android"))

        expectThat(sut.discoverState.value.filtersState.selectedPlatforms).isEqualTo(setOf(Platform(id = 21, label = "Android")))
        expectThat(sut.discoverState.value.searchQuery).isEqualTo("stra")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SearchQueryBasedGames>()

        sut.addToSelectedStores(Store(id = 4, label = "App store"))

        expectThat(sut.discoverState.value.filtersState.selectedStores).isEqualTo(setOf(Store(id = 4, label = "App store")))
        expectThat(sut.discoverState.value.filtersState.selectedPlatforms).isEqualTo(setOf(Platform(id = 21, label = "Android")))
        expectThat(sut.discoverState.value.searchQuery).isEqualTo("stra")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SearchQueryBasedGames>()

        sut.addToSelectedTags(Tag(id = 3, label = "Adventure"))

        expectThat(sut.discoverState.value.filtersState.selectedTags).isEqualTo(setOf(Tag(id = 3, label = "Adventure")))
        expectThat(sut.discoverState.value.filtersState.selectedStores).isEqualTo(setOf(Store(id = 4, label = "App store")))
        expectThat(sut.discoverState.value.filtersState.selectedPlatforms).isEqualTo(setOf(Platform(id = 21, label = "Android")))
        expectThat(sut.discoverState.value.searchQuery).isEqualTo("stra")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SearchQueryBasedGames>()

        sut.removeFromSelectedTags(Tag(id = 3, label = "Adventure"))
        sut.removeFromSelectedPlatforms(Platform(id = 21, label = "Android"))
        sut.removeFromSelectedStores(Store(id = 4, label = "App store"))
        sut.updateSearchQuery("")

        advanceUntilIdle()

        expectThat(sut.discoverState.value.filtersState.selectedTags).isEqualTo(emptySet())
        expectThat(sut.discoverState.value.filtersState.selectedStores).isEqualTo(emptySet())
        expectThat(sut.discoverState.value.filtersState.selectedPlatforms).isEqualTo(emptySet())
        expectThat(sut.discoverState.value.searchQuery).isEqualTo("")
        expectThat(sut.discoverState.value.gamesState).isA<DiscoverStateGames.SuggestedGames>()
    }

    @Test
    fun `given a list of games, when grouped by genre, we get a map of genre to list of games`() {
        expectThat(emptyList<Game>().groupByGenre()).isEqualTo(emptyMap())

        val gamesGroupedByGenre = games.groupByGenre()

        expectThat(gamesGroupedByGenre).isEqualTo(expectedGamesGroupedByGenre)

        expectThat(listOfNotNull(gameWithNoGenres).groupByGenre()).isEqualTo(mapOf(otherGenreWithGames))
    }
}
