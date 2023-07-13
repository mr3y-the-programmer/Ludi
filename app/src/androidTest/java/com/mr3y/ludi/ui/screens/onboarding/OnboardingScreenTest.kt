package com.mr3y.ludi.ui.screens.onboarding

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.data
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.theme.LudiTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnboardingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

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
    fun games_page_loads_successfully_games_are_visible_and_selectable_and_state_survives_config_changes() {
        // setup
        val restorationTester = StateRestorationTester(composeTestRule)
        restorationTester.setContent {
            val selectedGames = rememberSaveable(Unit) { mutableStateOf(emptyList<FavouriteGame>()) }

            LudiTheme {
                OnboardingScreen(
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
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).performClick()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_games_page_title)).assertIsDisplayed()

        composeTestRule.onNodeWithText("").assertIsDisplayed()
        composeTestRule.onNodeWithText("").assertIsEnabled()
        composeTestRule.onNodeWithText("").assertHasClickAction()
        composeTestRule.onNodeWithText("").assert(hasSetTextAction())

        val suggestedGames = FakeOnboardingGames.games.data!!
        repeat(suggestedGames.size) { index ->
            composeTestRule.onNodeWithText(suggestedGames[index].actualResource!!.name).assertIsDisplayed()
            composeTestRule.onNodeWithText(suggestedGames[index].actualResource!!.rating.toString()).assertIsDisplayed()
            composeTestRule.onNodeWithText(suggestedGames[index].actualResource!!.name).assertIsSelectable()
            composeTestRule.onNodeWithText(suggestedGames[index].actualResource!!.name).assertIsNotSelected()
        }

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()

        val randomSelectedGame = suggestedGames.random().actualResource!!
        composeTestRule.onNodeWithText(randomSelectedGame.name).performClick()
        composeTestRule.onNodeWithText(randomSelectedGame.name).assertIsSelected()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).assertIsDisplayed()
        composeTestRule.onNodeWithText(randomSelectedGame.name).performClick()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()
    }

    @Test
    fun data_sources_page_loads_successfully_data_sources_are_visible_and_selectable_and_state_survives_config_changes() {
        // setup
        val restorationTester = StateRestorationTester(composeTestRule)
        restorationTester.setContent {
            val selectedSources = rememberSaveable(Unit) { mutableStateOf(emptyList<NewsDataSource>()) }

            LudiTheme {
                OnboardingScreen(
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
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).performClick()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_continue)).performClick()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_data_sources_page_title)).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()

        val allSources = FakeOnboardingState.allNewsDataSources
        allSources.forEach { source ->
            composeTestRule.onNodeWithText(source.name).assertIsSelectable()
            composeTestRule.onNodeWithText(source.name).assertIsNotSelected()

            composeTestRule.onNodeWithText(source.name).performClick()
            composeTestRule.onNodeWithText(source.name).assertIsSelected()

            composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_finish)).assertIsDisplayed()
        }

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_finish)).assertIsDisplayed()

        allSources.forEachIndexed { index, source ->
            composeTestRule.onNodeWithText(source.name).assertIsSelectable()
            composeTestRule.onNodeWithText(source.name).assertIsSelected()

            composeTestRule.onNodeWithText(source.name).performClick()
            composeTestRule.onNodeWithText(source.name).assertIsNotSelected()

            if (index != allSources.lastIndex) {
                composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_finish)).assertIsDisplayed()
            } else {
                composeTestRule.onNodeWithText(context.resources.getString(R.string.on_boarding_fab_state_skip)).assertIsDisplayed()
            }
        }
    }
}
