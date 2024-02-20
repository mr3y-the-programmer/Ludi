package com.mr3y.ludi.shared.ui.screens.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.performClick
import cafe.adriel.lyricist.LocalStrings
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.shared.ui.resources.LudiStrings
import com.mr3y.ludi.shared.ui.screens.BaseRobolectricTest
import com.mr3y.ludi.shared.ui.screens.onNodeWithStateDescription
import com.mr3y.ludi.shared.ui.theme.LudiTheme
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OnboardingScreenFABTest : BaseRobolectricTest() {

    @Test
    fun genres_page_fab_state_survives_config_changes() {
        val restorationTester = StateRestorationTester(composeTestRule)
        var strings: LudiStrings? by mutableStateOf(null)
        restorationTester.setContent {
            val selectedGenres = rememberSaveable(Unit) { mutableStateOf(emptySet<GameGenre>()) }

            LudiTheme {
                strings = LocalStrings.current
                OnboardingScreen(
                    initialPage = 0,
                    onboardingState = FakeOnboardingState.copy(selectedGamingGenres = selectedGenres.value),
                    searchQuery = "",
                    onSkipButtonClicked = { /*TODO*/ },
                    onFinishButtonClicked = { /*TODO*/ },
                    onSelectingNewsDataSource = {},
                    onUnselectNewsDataSource = {},
                    onUpdatingSearchQueryText = {},
                    onAddingGameToFavourites = {},
                    onRemovingGameFromFavourites = {},
                    onRefreshingGames = {},
                    onSelectingGenre = { selectedGenres.value = selectedGenres.value + it },
                    onUnselectingGenre = { selectedGenres.value = selectedGenres.value - it },
                    onRefreshingGenres = {}
                )
            }
        }

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_skip_state_desc).assertIsDisplayed()

        val randomGenre = FakeAllGenres.data.random()
        composeTestRule.onGenre(strings!!.genres_page_genre_off_state_desc(randomGenre.name)).assertIsDisplayed()
        composeTestRule.onGenre(strings!!.genres_page_genre_off_state_desc(randomGenre.name)).performClick()

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_continue_state_desc).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_continue_state_desc).assertIsDisplayed()
    }

    @Test
    @Ignore("This robolectric test started to fail with \"java.lang.NoClassDefFoundError: androidx/compose/ui/platform/LocalSoftwareKeyboardController\" " +
            "after upgrading to compose multiplatform 1.5.12, I need to investigate this more to understand the root " +
            "cause, and why it happens only with LocalSoftwareKeyboardController." +
            "Similar issues: https://github.com/robolectric/robolectric/issues/8688")
    fun games_page_fab_state_survives_config_changes() {
        // setup
        val restorationTester = StateRestorationTester(composeTestRule)
        var strings: LudiStrings? by mutableStateOf(null)
        val favouriteGames = mutableStateOf(emptyList<FavouriteGame>())
        restorationTester.setContent {
            val selectedGames = rememberSaveable(Unit) { favouriteGames }

            LudiTheme {
                strings = LocalStrings.current
                OnboardingScreen(
                    initialPage = 1,
                    onboardingState = FakeOnboardingState.copy(favouriteGames = selectedGames.value),
                    searchQuery = "",
                    onSkipButtonClicked = { /*TODO*/ },
                    onFinishButtonClicked = { /*TODO*/ },
                    onSelectingNewsDataSource = {},
                    onUnselectNewsDataSource = {},
                    onUpdatingSearchQueryText = {},
                    onAddingGameToFavourites = { selectedGames.value = selectedGames.value + it },
                    onRemovingGameFromFavourites = { selectedGames.value = selectedGames.value - it },
                    onRefreshingGames = {},
                    onSelectingGenre = { },
                    onUnselectingGenre = { },
                    onRefreshingGenres = {}
                )
            }
        }

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_skip_state_desc).assertIsDisplayed()
        favouriteGames.value = FakeSelectedGames
        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_continue_state_desc).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_continue_state_desc).assertIsDisplayed()
    }

    @Test
    fun dataSources_page_fab_state_survives_config_changes() {
        // setup
        val restorationTester = StateRestorationTester(composeTestRule)
        var strings: LudiStrings? by mutableStateOf(null)
        val selectedDataSources = mutableStateOf(emptyList<NewsDataSource>())
        restorationTester.setContent {
            val selectedSources = rememberSaveable(Unit) { selectedDataSources }

            LudiTheme {
                strings = LocalStrings.current
                OnboardingScreen(
                    initialPage = 2,
                    onboardingState = FakeOnboardingState.copy(followedNewsDataSources = selectedSources.value),
                    searchQuery = "",
                    onSkipButtonClicked = { },
                    onFinishButtonClicked = { },
                    onSelectingNewsDataSource = { selectedSources.value = selectedSources.value + it },
                    onUnselectNewsDataSource = { selectedSources.value = selectedSources.value - it },
                    onUpdatingSearchQueryText = {},
                    onAddingGameToFavourites = {},
                    onRemovingGameFromFavourites = {},
                    onRefreshingGames = {},
                    onSelectingGenre = { },
                    onUnselectingGenre = { },
                    onRefreshingGenres = {}
                )
            }
        }
        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_skip_state_desc).assertIsDisplayed()
        selectedDataSources.value = listOf(FakeNewsDataSources.first())
        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_finish_state_desc).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_finish_state_desc).assertIsDisplayed()
    }
}
