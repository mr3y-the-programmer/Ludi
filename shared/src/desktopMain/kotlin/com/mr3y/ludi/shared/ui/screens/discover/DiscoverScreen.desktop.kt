package com.mr3y.ludi.shared.ui.screens.discover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mr3y.ludi.shared.ui.components.openUrlInBrowser
import com.mr3y.ludi.shared.ui.presenter.DiscoverViewModel

@Composable
actual fun DiscoverScreen(
    modifier: Modifier,
    viewModel: DiscoverViewModel
) {
    val discoverState by viewModel.discoverState.collectAsState()
    DiscoverScreen(
        discoverState = discoverState,
        searchQuery = viewModel.searchQuery.value,
        onUpdatingSearchQueryText = viewModel::updateSearchQuery,
        onSelectingPlatform = viewModel::addToSelectedPlatforms,
        onUnselectingPlatform = viewModel::removeFromSelectedPlatforms,
        onSelectingStore = viewModel::addToSelectedStores,
        onUnselectingStore = viewModel::removeFromSelectedStores,
        onSelectingTag = viewModel::addToSelectedTags,
        onUnselectingTag = viewModel::removeFromSelectedTags,
        onRefresh = viewModel::refresh,
        onOpenUrl = ::openUrlInBrowser,
        modifier = modifier
    )
}
