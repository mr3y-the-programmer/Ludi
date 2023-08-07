package com.mr3y.ludi.ui.screens.onboarding

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.theme.LudiTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class OnboardingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun onboarding_launches_navigation_between_pages_works_as_expected() {
        composeTestRule.setContent {
            LudiTheme {
                OnboardingScreen(
                    onboardingState = FakeOnboardingState,
                    onSkipButtonClicked = { /*TODO*/ },
                    onFinishButtonClicked = { /*TODO*/ },
                    onSelectingNewsDataSource = {},
                    onUnselectNewsDataSource = {},
                    onUpdatingSearchQueryText = {},
                    onAddingGameToFavourites = {},
                    onRemovingGameFromFavourites = {},
                    onSelectingGenre = {},
                    onUnselectingGenre = {}
                )
            }
        }

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_genres_page_title)).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_games_page_title)).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_data_sources_page_title)).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_finish)).assertIsDisplayed()
    }

    @Test
    fun genres_page_loads_successfully_genres_are_visible_and_selectable_and_state_survives_config_changes() {
        val restorationTester = StateRestorationTester(composeTestRule)
        restorationTester.setContent {
            val selectedGenres = rememberSaveable(Unit) { mutableStateOf(emptySet<GameGenre>()) }

            LudiTheme {
                OnboardingScreen(
                    initialPage = 0,
                    onboardingState = FakeOnboardingState.copy(selectedGamingGenres = selectedGenres.value),
                    onSkipButtonClicked = { /*TODO*/ },
                    onFinishButtonClicked = { /*TODO*/ },
                    onSelectingNewsDataSource = {},
                    onUnselectNewsDataSource = {},
                    onUpdatingSearchQueryText = {},
                    onAddingGameToFavourites = {},
                    onRemovingGameFromFavourites = {},
                    onSelectingGenre = { selectedGenres.value = selectedGenres.value + it },
                    onUnselectingGenre = { selectedGenres.value = selectedGenres.value - it }
                )
            }
        }

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_genres_page_title)).assertIsDisplayed()

        val allGenres = FakeAllGenres.data.resource
        allGenres.forEach { genre ->
            composeTestRule.onNodeWithText(genre.name).assertIsDisplayed()
            composeTestRule.onNodeWithText(genre.name).assertIsSelectable()
            composeTestRule.onNodeWithText(genre.name).assertIsNotSelected()
        }

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()

        val randomSelectedGenre = allGenres.random().name
        composeTestRule.onNodeWithText(randomSelectedGenre).performClick()
        composeTestRule.onNodeWithText(randomSelectedGenre).assertIsSelected()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).assertIsDisplayed()
        composeTestRule.onNodeWithText(randomSelectedGenre).performClick()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()
    }

    @Test
    fun fab_state_changes_based_on_selected_games_value_and_it_survives_config_changes() {
        // setup
        val restorationTester = StateRestorationTester(composeTestRule)
        val favouriteGames = mutableStateOf(emptyList<FavouriteGame>())
        restorationTester.setContent {
            val selectedGames = rememberSaveable(Unit) { favouriteGames }

            LudiTheme {
                OnboardingScreen(
                    initialPage = 1,
                    onboardingState = FakeOnboardingState.copy(favouriteGames = selectedGames.value),
                    onSkipButtonClicked = { /*TODO*/ },
                    onFinishButtonClicked = { /*TODO*/ },
                    onSelectingNewsDataSource = {},
                    onUnselectNewsDataSource = {},
                    onUpdatingSearchQueryText = {},
                    onAddingGameToFavourites = { selectedGames.value = selectedGames.value + it },
                    onRemovingGameFromFavourites = { selectedGames.value = selectedGames.value - it },
                    onSelectingGenre = { },
                    onUnselectingGenre = { }
                )
            }
        }

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()
        favouriteGames.value = FakeSelectedGames
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).assertIsDisplayed()
        favouriteGames.value = emptyList()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()
    }

    @Test
    fun fab_state_changes_based_on_selected_dataSources_value_and_it_survives_config_changes() {
        // setup
        val restorationTester = StateRestorationTester(composeTestRule)
        val selectedDataSources = mutableStateOf(emptyList<NewsDataSource>())
        restorationTester.setContent {
            val selectedSources = rememberSaveable(Unit) { selectedDataSources }

            LudiTheme {
                OnboardingScreen(
                    initialPage = 2,
                    onboardingState = FakeOnboardingState.copy(followedNewsDataSources = selectedSources.value),
                    onSkipButtonClicked = { },
                    onFinishButtonClicked = { },
                    onSelectingNewsDataSource = { selectedSources.value = selectedSources.value + it },
                    onUnselectNewsDataSource = { selectedSources.value = selectedSources.value - it },
                    onUpdatingSearchQueryText = {},
                    onAddingGameToFavourites = {},
                    onRemovingGameFromFavourites = {},
                    onSelectingGenre = { },
                    onUnselectingGenre = { }
                )
            }
        }
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()
        selectedDataSources.value = listOf(FakeNewsDataSources.first())
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_finish)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_finish)).assertIsDisplayed()
        selectedDataSources.value = emptyList()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()
    }
}
