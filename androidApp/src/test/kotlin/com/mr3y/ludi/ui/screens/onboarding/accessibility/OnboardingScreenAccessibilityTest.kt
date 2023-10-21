package com.mr3y.ludi.ui.screens.onboarding.accessibility

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasPerformImeAction
import androidx.compose.ui.test.isFocusable
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.paging.testing.asSnapshot
import cafe.adriel.lyricist.LocalStrings
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.shared.ui.resources.LudiStrings
import com.mr3y.ludi.ui.screens.BaseRobolectricTest
import com.mr3y.ludi.ui.screens.onNodeWithStateDescription
import com.mr3y.ludi.ui.screens.onboarding.FakeAllGenres
import com.mr3y.ludi.ui.screens.onboarding.FakeNewsDataSources
import com.mr3y.ludi.ui.screens.onboarding.FakeOnboardingGames
import com.mr3y.ludi.ui.screens.onboarding.FakeOnboardingState
import com.mr3y.ludi.shared.ui.screens.onboarding.OnboardingScreen
import com.mr3y.ludi.ui.screens.onboarding.onGenre
import com.mr3y.ludi.shared.ui.theme.LudiTheme
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OnboardingScreenAccessibilityTest : BaseRobolectricTest() {

    @Test
    fun genres_page_launches_nodes_have_semantics_for_a11y_services() {
        // Setup
        var strings: LudiStrings? by mutableStateOf(null)
        composeTestRule.setContent {
            val selectedGenres = rememberSaveable(Unit) { mutableStateOf(emptySet<GameGenre>()) }

            LudiTheme {
                strings = LocalStrings.current
                OnboardingScreen(
                    onboardingState = FakeOnboardingState.copy(selectedGamingGenres = selectedGenres.value),
                    searchQuery = "",
                    initialPage = 0,
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

        val header = buildString {
            append(strings!!.on_boarding_genres_page_title)
            append("\n")
            append(strings!!.on_boarding_secondary_text)
        }
        composeTestRule.onNodeWithContentDescription(header).assertIsDisplayed()

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_skip_state_desc).assertIsDisplayed()

        val randomGenre = FakeAllGenres.data.random()
        composeTestRule.onGenre(strings!!.genres_page_genre_off_state_desc(randomGenre.name)).apply {
            assertIsDisplayed()
            assertIsNotSelected()
            performClick()
        }

        composeTestRule.onGenre(strings!!.genres_page_genre_on_state_desc(randomGenre.name)).apply {
            assertIsDisplayed()
            assertIsSelected()
        }

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_continue_state_desc).assertIsDisplayed()
    }

    @Test
    fun games_page_launches_nodes_have_semantics_for_a11y_services() = runTest {
        // Setup
        var strings: LudiStrings? by mutableStateOf(null)
        composeTestRule.setContent {
            val selectedGames = rememberSaveable(Unit) { mutableStateOf(emptyList<FavouriteGame>()) }

            LudiTheme {
                strings = LocalStrings.current
                OnboardingScreen(
                    onboardingState = FakeOnboardingState.copy(favouriteGames = selectedGames.value, selectedGamingGenres = emptySet()),
                    searchQuery = "",
                    initialPage = 1,
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

        val header = buildString {
            append(strings!!.on_boarding_games_page_title)
            append("\n")
            append(strings!!.on_boarding_secondary_text)
        }
        composeTestRule.onNodeWithContentDescription(header).assertIsDisplayed()

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_skip_state_desc).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(strings!!.games_page_search_field_desc).apply {
            assertIsDisplayed()
            assert(isFocusable())
            assert(hasPerformImeAction())
        }

        val randomGame = FakeOnboardingGames.games.asSnapshot().random()
        composeTestRule.onNodeWithStateDescription(strings!!.games_page_game_off_state_desc(randomGame.name)).apply {
            // Ensure the composable is fully visible within the viewport
            onParent().performScrollTo()

            assertIsDisplayed()
            assertIsNotSelected()
            performClick()
        }

        composeTestRule.onNodeWithStateDescription(strings!!.games_page_game_on_state_desc(randomGame.name)).apply {
            // Ensure the composable is fully visible within the viewport
            onParent().performScrollTo()

            assertIsDisplayed()
            assertIsSelected()
        }

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_continue_state_desc).assertIsDisplayed()
    }

    @Test
    fun dataSources_page_launches_nodes_have_semantics_for_a11y_services() {
        // Setup
        var strings: LudiStrings? by mutableStateOf(null)
        composeTestRule.setContent {
            val selectedSources = rememberSaveable(Unit) { mutableStateOf(emptyList<NewsDataSource>()) }

            LudiTheme {
                strings = LocalStrings.current
                OnboardingScreen(
                    onboardingState = FakeOnboardingState.copy(followedNewsDataSources = selectedSources.value),
                    searchQuery = "",
                    initialPage = 2,
                    onSkipButtonClicked = { /*TODO*/ },
                    onFinishButtonClicked = { /*TODO*/ },
                    onSelectingNewsDataSource = { selectedSources.value = selectedSources.value + it },
                    onUnselectNewsDataSource = { selectedSources.value = selectedSources.value - it },
                    onUpdatingSearchQueryText = {},
                    onAddingGameToFavourites = { },
                    onRemovingGameFromFavourites = { },
                    onRefreshingGames = {},
                    onSelectingGenre = { },
                    onUnselectingGenre = { },
                    onRefreshingGenres = {}
                )
            }
        }

        val header = buildString {
            append(strings!!.on_boarding_data_sources_page_title)
            append("\n")
            append(strings!!.on_boarding_secondary_text)
        }
        composeTestRule.onNodeWithContentDescription(header).assertIsDisplayed()

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_skip_state_desc).assertIsDisplayed()

        val randomDataSource = FakeNewsDataSources.random()
        composeTestRule.onNodeWithStateDescription(strings!!.data_sources_page_data_source_off_state_desc(randomDataSource.name)).apply {
            assertIsDisplayed()
            assertIsNotSelected()
            performClick()
        }

        composeTestRule.onNodeWithStateDescription(strings!!.data_sources_page_data_source_on_state_desc(randomDataSource.name)).apply {
            assertIsDisplayed()
            assertIsSelected()
        }

        composeTestRule.onNodeWithStateDescription(strings!!.on_boarding_fab_state_finish_state_desc).assertIsDisplayed()
    }
}
