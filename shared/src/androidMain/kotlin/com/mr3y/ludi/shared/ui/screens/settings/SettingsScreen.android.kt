package com.mr3y.ludi.shared.ui.screens.settings

import android.net.Uri
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.shared.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.shared.ui.components.launchChromeCustomTab
import com.mr3y.ludi.shared.ui.presenter.SettingsViewModel
import com.mr3y.ludi.shared.ui.theme.isDynamicColorSupported

@Composable
actual fun SettingsScreen(
    onFollowedNewsDataSourcesClick: () -> Unit,
    onFavouriteGamesClick: () -> Unit,
    onFavouriteGenresClick: () -> Unit,
    modifier: Modifier,
    viewModel: SettingsViewModel
) {
    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val tabToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
    SettingsScreen(
        settingsState,
        modifier = modifier,
        onFollowedNewsDataSourcesClick = onFollowedNewsDataSourcesClick,
        onFavouriteGamesClick = onFavouriteGamesClick,
        onFavouriteGenresClick = onFavouriteGenresClick,
        onUpdateTheme = viewModel::setAppTheme,
        onToggleDynamicColorValue = viewModel::enableUsingDynamicColor,
        onOpenUrl = { url ->
            launchChromeCustomTab(context, Uri.parse(url), tabToolbarColor)
        }
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
actual fun isDynamicColorEnabled(): Boolean {
    return isDynamicColorSupported() && Build.VERSION.SDK_INT >= 31
}
