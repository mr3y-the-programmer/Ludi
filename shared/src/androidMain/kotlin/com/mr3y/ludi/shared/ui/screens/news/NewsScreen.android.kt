package com.mr3y.ludi.shared.ui.screens.news

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.shared.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.shared.ui.components.launchChromeCustomTab
import com.mr3y.ludi.shared.ui.presenter.NewsViewModel

@Composable
actual fun NewsScreen(
    onTuneClick: () -> Unit,
    modifier: Modifier,
    viewModel: NewsViewModel
) {
    val newsState by viewModel.newsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val tabToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
    NewsScreen(
        newsState = newsState,
        onTuneClick = onTuneClick,
        onRefresh = viewModel::refresh,
        onOpenUrl = { url ->
            launchChromeCustomTab(context, Uri.parse(url), tabToolbarColor)
        },
        onConsumeEvent = viewModel::consumeCurrentEvent,
        modifier = modifier
    )
}
