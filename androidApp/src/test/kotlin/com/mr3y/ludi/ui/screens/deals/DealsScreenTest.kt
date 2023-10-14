package com.mr3y.ludi.ui.screens.deals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cafe.adriel.lyricist.LocalStrings
import com.mr3y.ludi.shared.ui.resources.LudiStrings
import com.mr3y.ludi.shared.ui.screens.deals.DealsScreen
import com.mr3y.ludi.ui.screens.BaseRobolectricTest
import com.mr3y.ludi.shared.ui.theme.LudiTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DealsScreenTest : BaseRobolectricTest() {

    @Test
    fun deals_launches_state_is_saved_and_survives_config_changes() {
        val restorationTester = StateRestorationTester(composeTestRule)
        var strings: LudiStrings? by mutableStateOf(null)
        var selectedTab by mutableStateOf(0)
        var showFilters by mutableStateOf(false)
        restorationTester.setContent {
            LudiTheme {
                strings = LocalStrings.current
                DealsScreen(
                    dealsState = FakeDealsState.copy(selectedTab = selectedTab, showFilters = showFilters),
                    searchQuery = "",
                    onUpdateSearchQuery = {},
                    onSelectingDealStore = {},
                    onUnselectingDealStore = {},
                    onSelectingGiveawayStore = {},
                    onUnselectingGiveawayStore = {},
                    onSelectingGiveawayPlatform = {},
                    onUnselectingGiveawayPlatform = {},
                    onRefreshDeals = {},
                    onRefreshGiveaways = {},
                    onSelectTab = { selectedTab = it },
                    onToggleFilters = { showFilters = !showFilters },
                    onOpenUrl = {},
                )
            }
        }

        composeTestRule.onNodeWithText(strings!!.deals_label).assertIsDisplayed()

        composeTestRule.onNodeWithText(strings!!.giveaways_label).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription(strings!!.deals_page_search_field_content_description).assertIsDisplayed()

        // Assert the deals are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeDealSamples.first().name))

        composeTestRule.onNodeWithText(strings!!.giveaways_label).performClick()

        // Assert the giveaways are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeGiveawaysSamples.first().title))

        composeTestRule.onNodeWithContentDescription(strings!!.deals_filter_icon_content_description).performClick()

        composeTestRule.onNodeWithText(strings!!.close_filters).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(strings!!.close_filters).assertIsDisplayed()
        composeTestRule.onNodeWithText(strings!!.close_filters).performClick()

        // Assert the giveaways are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeGiveawaysSamples.first().title))
    }
}
