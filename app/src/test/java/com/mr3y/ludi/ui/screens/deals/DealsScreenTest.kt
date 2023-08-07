package com.mr3y.ludi.ui.screens.deals

import android.content.Context
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.mr3y.ludi.R
import com.mr3y.ludi.ui.theme.LudiTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class DealsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

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

        composeTestRule.onNodeWithText(context.resources.getString(R.string.deals_label)).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.giveaways_label)).assertIsDisplayed()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.deals_search_filter_bar_placeholder)).assertIsDisplayed()

        // Assert the deals are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeDealSamples.first().resource.name))

        composeTestRule.onNodeWithText(context.resources.getString(R.string.giveaways_label)).performClick()

        // Assert the giveaways are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeGiveawaysSamples.first().resource.title))

        composeTestRule.onNodeWithContentDescription(context.resources.getString(R.string.deals_filter_icon_content_description)).performClick()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.close_filters)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithText(context.resources.getString(R.string.close_filters)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.resources.getString(R.string.close_filters)).performClick()

        // Assert the giveaways are visible in a scrollable lazy list
        composeTestRule.onNode(hasScrollToIndexAction())
            .onChildren()
            .onFirst()
            .assert(hasText(FakeGiveawaysSamples.first().resource.title))
    }
}
