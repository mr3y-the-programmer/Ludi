package com.mr3y.ludi.shared.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mr3y.ludi.shared.ui.components.openUrlInBrowser
import com.mr3y.ludi.shared.ui.presenter.SettingsViewModel

@Composable
actual fun SettingsScreen(
    onFollowedNewsDataSourcesClick: () -> Unit,
    onFavouriteGamesClick: () -> Unit,
    onFavouriteGenresClick: () -> Unit,
    modifier: Modifier,
    viewModel: SettingsViewModel
) {
    val settingsState by viewModel.settingsState.collectAsState()
    SettingsScreen(
        settingsState,
        modifier = modifier,
        onFollowedNewsDataSourcesClick = onFollowedNewsDataSourcesClick,
        onFavouriteGamesClick = onFavouriteGamesClick,
        onFavouriteGenresClick = onFavouriteGenresClick,
        onUpdateTheme = viewModel::setAppTheme,
        onToggleDynamicColorValue = {},
        onOpenUrl = ::openUrlInBrowser
    )
}

actual fun isDynamicColorEnabled(): Boolean = false
