package com.mr3y.ludi.shared.ui.screens.deals

import android.net.Uri
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.shared.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.shared.ui.components.launchChromeCustomTab
import com.mr3y.ludi.shared.ui.presenter.DealsViewModel

@Composable
actual fun DealsScreen(
    modifier: Modifier,
    viewModel: DealsViewModel
) {
    val dealsState by viewModel.dealsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val tabToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
    DealsScreen(
        dealsState = dealsState,
        searchQuery = viewModel.searchQuery.value,
        modifier = modifier.statusBarsPadding(),
        onUpdateSearchQuery = viewModel::updateSearchQuery,
        onSelectingDealStore = viewModel::addToSelectedDealsStores,
        onUnselectingDealStore = viewModel::removeFromSelectedDealsStores,
        onSelectingGiveawayStore = viewModel::addToSelectedGiveawaysStores,
        onUnselectingGiveawayStore = viewModel::removeFromSelectedGiveawaysStores,
        onSelectingGiveawayPlatform = viewModel::addToSelectedGiveawaysPlatforms,
        onUnselectingGiveawayPlatform = viewModel::removeFromSelectedGiveawayPlatforms,
        onRefreshDeals = viewModel::refreshDeals,
        onRefreshDealsFinished = viewModel::refreshDealsComplete,
        onRefreshGiveaways = viewModel::refreshGiveaways,
        onSelectTab = viewModel::selectTab,
        onOpenUrl = { url ->
            launchChromeCustomTab(context, Uri.parse(url), tabToolbarColor)
        }
    )
}
