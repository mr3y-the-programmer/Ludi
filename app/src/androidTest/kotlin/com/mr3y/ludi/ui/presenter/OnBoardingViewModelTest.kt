package com.mr3y.ludi.ui.presenter

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.datastore.model.UserFavouriteGames
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.MainDispatcherRule
import com.mr3y.ludi.ui.datastore.FavouriteGamesSerializer
import com.mr3y.ludi.ui.datastore.FavouriteGenresSerializer
import com.mr3y.ludi.ui.datastore.FollowedNewsDataSourceSerializer
import com.mr3y.ludi.ui.datastore.PreferencesKeys
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEmpty

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class OnBoardingViewModelTest {

    @get:Rule(order = 0)
    val tempFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())
    private val testScope = TestScope(testDispatcher)

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher(testDispatcher.scheduler))

    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources> = DataStoreFactory.create(
        serializer = FollowedNewsDataSourceSerializer,
        scope = testScope,
        produceFile = { tempFolder.newFile() }
    )
    private val favGamesStore: DataStore<UserFavouriteGames> = DataStoreFactory.create(
        serializer = FavouriteGamesSerializer,
        scope = testScope,
        produceFile = { tempFolder.newFile() }
    )
    private val favGenresStore: DataStore<UserFavouriteGenres> = DataStoreFactory.create(
        serializer = FavouriteGenresSerializer,
        scope = testScope,
        produceFile = { tempFolder.newFile() }
    )
    private val userPreferences: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { tempFolder.newFile("user_preferences_test.preferences_pb") }
    )

    private lateinit var sut: OnBoardingViewModel

    @Before
    fun setUp() {
        sut = OnBoardingViewModel(
            gamesRepository = FakeGamesRepository(),
            favGamesStore = favGamesStore,
            followedNewsDataSourcesStore = followedNewsDataSourcesStore,
            favGenresStore = favGenresStore,
            userPreferences = userPreferences
        )
        testScope.launch {
            followedNewsDataSourcesStore.updateData { it.toBuilder().clear().build() }
            favGamesStore.updateData { it.toBuilder().clear().build() }
            favGenresStore.updateData { it.toBuilder().clear().build() }
            userPreferences.edit { it.clear() }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun user_updates_preferences_new_preferences_are_saved_and_state_is_observable() = testScope.runTest {
        // stateIn(SharingStarted.Lazily) should have at least one subscriber to start collecting the upstream flow & share the values downstream
        backgroundScope.launch {
            sut.onboardingState.collect()
        }
        expectThat(sut.onboardingState.value).isEqualTo(OnBoardingViewModel.InitialOnboardingState)

        advanceUntilIdle()

        val suggestedGames = sut.onboardingState.value.onboardingGames.games
        expectThat(suggestedGames).isA<Result.Success<List<Game>>>()
        suggestedGames as Result.Success<List<Game>>
        expectThat(suggestedGames.data).isNotEmpty()

        val favouriteGame = FavouriteGame(id = 3498, title = "Grand Theft Auto V", imageUrl = "https://media.rawg.io/media/games/456/456dea5e1c7e3cd07060c14e96612001.jpg", rating = 4.47f)
        // make sure the function is idempotent
        repeat(2) { sut.addGameToFavourites(favouriteGame) }

        advanceUntilIdle()

        expectThat(sut.onboardingState.value.favouriteGames).isEqualTo(listOf(favouriteGame))

        val followedNewsDataSource = NewsDataSource("Game spot", R.drawable.game_spot_logo, Source.GameSpot)
        // make sure the function is idempotent
        repeat(2) { sut.followNewsDataSource(followedNewsDataSource) }
        repeat(2) { sut.followNewsDataSource(followedNewsDataSource.copy(name = "foo bar", R.drawable.giant_bomb_logo)) }

        advanceUntilIdle()

        expectThat(sut.onboardingState.value.favouriteGames).isEqualTo(listOf(favouriteGame))
        expectThat(sut.onboardingState.value.followedNewsDataSources).isEqualTo(listOf(followedNewsDataSource))

        val selectedGenre = GameGenre(id = 2, name = "Adventure", slug = "adventure", gamesCount = 2000, imageUrl = "https://media.rawg.io/media/games/f46/f466571d536f2e3ea9e815ad17177501.jpg")
        // make sure the function is idempotent
        repeat(2) { sut.selectGenre(selectedGenre) }

        advanceUntilIdle()

        expectThat(sut.onboardingState.value.favouriteGames).isEqualTo(listOf(favouriteGame))
        expectThat(sut.onboardingState.value.followedNewsDataSources).isEqualTo(listOf(followedNewsDataSource))
        expectThat(sut.onboardingState.value.selectedGamingGenres).isEqualTo(setOf(selectedGenre))

        // make sure the function is idempotent
        repeat(2) { sut.updateSearchQuery("Stray") }

        advanceUntilIdle()

        expectThat(sut.onboardingState.value.searchQuery).isEqualTo("Stray")
        expectThat(sut.onboardingState.value.favouriteGames).isEqualTo(listOf(favouriteGame))
        expectThat(sut.onboardingState.value.followedNewsDataSources).isEqualTo(listOf(followedNewsDataSource))
        expectThat(sut.onboardingState.value.selectedGamingGenres).isEqualTo(setOf(selectedGenre))

        // Also, make sure removing is idempotent
        repeat(2) { sut.unFollowNewsDataSource(followedNewsDataSource) }
        repeat(2) { sut.updateSearchQuery("") }
        repeat(2) { sut.removeGameFromFavourites(favouriteGame) }
        repeat(2) { sut.unselectGenre(selectedGenre) }

        advanceUntilIdle()

        expectThat(sut.onboardingState.value.searchQuery).isEqualTo("")
        expectThat(sut.onboardingState.value.favouriteGames).isEqualTo(emptyList())
        expectThat(sut.onboardingState.value.followedNewsDataSources).isEqualTo(emptyList())
        expectThat(sut.onboardingState.value.selectedGamingGenres).isEqualTo(emptySet())
    }

    @Test
    fun user_completes_onboarding_successfully_onboarding_finishes() = testScope.runTest {
        userPreferences.edit {
            it[PreferencesKeys.OnBoardingScreenKey] = true
        }

        sut.completeOnboarding()
        advanceUntilIdle()

        expectThat(userPreferences.data.first()[PreferencesKeys.OnBoardingScreenKey]).isEqualTo(false)
    }

    @After
    fun teardown() {
        testScope.launch {
            followedNewsDataSourcesStore.updateData {
                it.toBuilder().clear().build()
            }
            favGamesStore.updateData {
                it.toBuilder().clear().build()
            }
            favGenresStore.updateData {
                it.toBuilder().clear().build()
            }
            userPreferences.edit { it.clear() }
        }
    }
}
