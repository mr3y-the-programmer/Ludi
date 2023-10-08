package com.mr3y.ludi.shared.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.shared.ui.presenter.EditPreferencesViewModel

@Composable
actual fun EditPreferencesScreen(
    onDoneClick: () -> Unit,
    modifier: Modifier,
    viewModel: EditPreferencesViewModel
) {
    val editPreferencesState by viewModel.editPreferencesState.collectAsStateWithLifecycle()
    EditPreferencesScreen(
        editPreferencesState,
        onDoneClick = onDoneClick,
        onFollowingNewsDataSource = viewModel::followNewsDataSource,
        onUnfollowingNewsDataSource = viewModel::unFollowNewsDataSource,
        onRemovingGameFromFavourites = viewModel::removeGameFromFavourites,
        onAddingGenreToFavourites = viewModel::addToSelectedGenres,
        onRemovingGenreFromFavourites = viewModel::removeFromSelectedGenres,
        modifier = modifier
    )
}
