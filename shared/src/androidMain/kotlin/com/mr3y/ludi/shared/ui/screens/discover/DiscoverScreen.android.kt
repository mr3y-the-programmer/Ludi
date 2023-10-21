package com.mr3y.ludi.shared.ui.screens.discover

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.shared.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.shared.ui.components.launchChromeCustomTab
import com.mr3y.ludi.shared.ui.presenter.DiscoverViewModel

@Composable
actual fun DiscoverScreen(
    modifier: Modifier,
    viewModel: DiscoverViewModel
) {
    val discoverState by viewModel.discoverState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val tabToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
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
        onOpenUrl = { url ->
            launchChromeCustomTab(context, Uri.parse(url), tabToolbarColor)
        },
        modifier = modifier
    )
}
