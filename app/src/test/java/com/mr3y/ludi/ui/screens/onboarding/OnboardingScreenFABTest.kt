package com.mr3y.ludi.ui.screens.onboarding

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.performClick
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.screens.BaseRobolectricTest
import com.mr3y.ludi.ui.screens.getString
import com.mr3y.ludi.ui.screens.onNodeWithStateDescription
import com.mr3y.ludi.ui.theme.LudiTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OnboardingScreenFABTest : BaseRobolectricTest() {

    @Test
    fun genres_page_fab_state_survives_config_changes() {
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

        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_skip_state_desc)).assertIsDisplayed()

        val randomGenre = FakeAllGenres.data.random()
        composeTestRule.onGenre(R.string.genres_page_genre_off_state_desc, randomGenre.name).assertIsDisplayed()
        composeTestRule.onGenre(R.string.genres_page_genre_off_state_desc, randomGenre.name).performClick()

        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_continue_state_desc)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_continue_state_desc)).assertIsDisplayed()
    }

    @Test
    fun games_page_fab_state_survives_config_changes() {
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

        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_skip_state_desc)).assertIsDisplayed()
        favouriteGames.value = FakeSelectedGames
        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_continue_state_desc)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_continue_state_desc)).assertIsDisplayed()
    }

    @Test
    fun dataSources_page_fab_state_survives_config_changes() {
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
        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_skip_state_desc)).assertIsDisplayed()
        selectedDataSources.value = listOf(FakeNewsDataSources.first())
        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_finish_state_desc)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithStateDescription(getString(R.string.on_boarding_fab_state_finish_state_desc)).assertIsDisplayed()
    }
}
