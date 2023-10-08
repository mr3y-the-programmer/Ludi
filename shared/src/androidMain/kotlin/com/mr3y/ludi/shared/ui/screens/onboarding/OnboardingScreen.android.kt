package com.mr3y.ludi.shared.ui.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.shared.ui.presenter.OnBoardingViewModel

@Composable
actual fun OnboardingScreen(
    modifier: Modifier,
    viewModel: OnBoardingViewModel
) {
    val onboardingState by viewModel.onboardingState.collectAsStateWithLifecycle()
    OnboardingScreen(
        modifier = modifier,
        onboardingState = onboardingState,
        onSkipButtonClicked = viewModel::completeOnboarding,
        onFinishButtonClicked = viewModel::completeOnboarding,
        onSelectingNewsDataSource = viewModel::followNewsDataSource,
        onUnselectNewsDataSource = viewModel::unFollowNewsDataSource,
        onUpdatingSearchQueryText = viewModel::updateSearchQuery,
        onAddingGameToFavourites = viewModel::addGameToFavourites,
        onRemovingGameFromFavourites = viewModel::removeGameFromFavourites,
        onRefreshingGames = viewModel::refreshGames,
        onSelectingGenre = viewModel::selectGenre,
        onUnselectingGenre = viewModel::unselectGenre,
        onRefreshingGenres = viewModel::refreshGenres
    )
}