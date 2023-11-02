package com.mr3y.ludi.shared.ui.screens.deals

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mr3y.ludi.shared.ui.components.openUrlInBrowser
import com.mr3y.ludi.shared.ui.presenter.DealsViewModel

@Composable
actual fun DealsScreen(
    modifier: Modifier,
    viewModel: DealsViewModel
) {
    val dealsState by viewModel.dealsState.collectAsState()
    DealsScreen(
        dealsState = dealsState,
        searchQuery = viewModel.searchQuery.value,
        modifier = modifier,
        onUpdateSearchQuery = viewModel::updateSearchQuery,
        onSelectingDealStore = viewModel::addToSelectedDealsStores,
        onUnselectingDealStore = viewModel::removeFromSelectedDealsStores,
        onSelectingGiveawayStore = viewModel::addToSelectedGiveawaysStores,
        onUnselectingGiveawayStore = viewModel::removeFromSelectedGiveawaysStores,
        onSelectingGiveawayPlatform = viewModel::addToSelectedGiveawaysPlatforms,
        onUnselectingGiveawayPlatform = viewModel::removeFromSelectedGiveawayPlatforms,
        onRefreshDeals = viewModel::refreshDeals,
        onRefreshGiveaways = viewModel::refreshGiveaways,
        onSelectTab = viewModel::selectTab,
        onOpenUrl = ::openUrlInBrowser
    )
}
