package com.mr3y.ludi.ui.screens.deals

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
import com.mr3y.ludi.R
import com.mr3y.ludi.ui.screens.BaseRobolectricTest
import com.mr3y.ludi.ui.screens.getString
import com.mr3y.ludi.ui.theme.LudiTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DealsScreenTest : BaseRobolectricTest() {

    @Test
    fun deals_launches_state_is_saved_and_survives_config_changes() {
        val restorationTester = StateRestorationTester(composeTestRule)
        restorationTester.setContent {
            LudiTheme {
                DealsScreen(
                    dealsState = FakeDealsState,
                    onUpdateSearchQuery = {},
                    onSelectingDealStore = {},
                    onUnselectingDealStore = {},
                    onSelectingGiveawayStore = {},
                    onUnselectingGiveawayStore = {},
                    onSelectingGiveawayPlatform = {},
                    onUnselectingGiveawayPlatform = {}
                )
            }
        }

        composeTestRule.onNodeWithText(getString(R.string.deals_label)).assertIsDisplayed()

        composeTestRule.onNodeWithText(getString(R.string.giveaways_label)).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription(getString(R.string.deals_page_search_field_content_description)).assertIsDisplayed()

        // Assert the deals are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeDealSamples.first().resource.name))

        composeTestRule.onNodeWithText(getString(R.string.giveaways_label)).performClick()

        // Assert the giveaways are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeGiveawaysSamples.first().resource.title))

        composeTestRule.onNodeWithContentDescription(getString(R.string.deals_filter_icon_content_description)).performClick()

        composeTestRule.onNodeWithText(getString(R.string.close_filters)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(getString(R.string.close_filters)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.close_filters)).performClick()

        // Assert the giveaways are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeGiveawaysSamples.first().resource.title))
    }
}
