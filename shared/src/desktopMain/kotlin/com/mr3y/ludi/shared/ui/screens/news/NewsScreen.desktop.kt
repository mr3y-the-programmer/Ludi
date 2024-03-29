package com.mr3y.ludi.shared.ui.screens.news

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mr3y.ludi.shared.ui.components.openUrlInBrowser
import com.mr3y.ludi.shared.ui.presenter.NewsViewModel

@Composable
actual fun NewsScreen(
    onTuneClick: () -> Unit,
    modifier: Modifier,
    viewModel: NewsViewModel
) {
    val newsState by viewModel.newsState.collectAsState()
    NewsScreen(
        newsState = newsState,
        searchQuery = viewModel.searchQuery.value,
        onSearchQueryValueChanged = viewModel::updateSearchQuery,
        onTuneClick = onTuneClick,
        onRefresh = viewModel::refresh,
        onConsumeEvent = viewModel::consumeCurrentEvent,
        onOpenUrl = ::openUrlInBrowser,
        modifier = modifier
    )
}
