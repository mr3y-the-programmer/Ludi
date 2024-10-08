package com.mr3y.ludi.shared.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.voyager.core.screen.Screen
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.di.getScreenModel
import com.mr3y.ludi.shared.ui.presenter.OnBoardingViewModel
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.shared.ui.presenter.model.OnboardingState
import kotlinx.coroutines.launch

const val OnboardingPagesCount = 3

object OnboardingScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<OnBoardingViewModel>()
        OnboardingScreen(modifier = Modifier.fillMaxSize(), viewModel = screenModel)
    }
}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnBoardingViewModel
) {
    val onboardingState by viewModel.onboardingState.collectAsStateWithLifecycle()
    OnboardingScreen(
        modifier = modifier,
        searchQuery = viewModel.searchQuery.value,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OnboardingScreen(
    onboardingState: OnboardingState,
    searchQuery: String,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    onSkipButtonClicked: () -> Unit,
    onFinishButtonClicked: () -> Unit,
    onSelectingNewsDataSource: (NewsDataSource) -> Unit,
    onUnselectNewsDataSource: (NewsDataSource) -> Unit,
    onUpdatingSearchQueryText: (String) -> Unit,
    onAddingGameToFavourites: (FavouriteGame) -> Unit,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
    onRefreshingGames: () -> Unit,
    onSelectingGenre: (GameGenre) -> Unit,
    onUnselectingGenre: (GameGenre) -> Unit,
    onRefreshingGenres: () -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = ::OnboardingPagesCount,
        initialPage = initialPage
    )
    var refresh by rememberSaveable(Unit) { mutableIntStateOf(0) }
    val refreshState = rememberPullRefreshState(
        refreshing = when (pagerState.currentPage) {
            0 -> onboardingState.isRefreshingGenres
            1 -> onboardingState.isRefreshingGames
            else -> false
        },
        onRefresh = {
            when (pagerState.currentPage) {
                0 -> onRefreshingGenres()
                1 -> onRefreshingGames()
            }
            refresh++
        }
    )
    Scaffold(
        modifier = modifier
            .pullRefresh(refreshState)
            .fillMaxSize()
            .imePadding(),
        bottomBar = {
            val indicatorProgress by remember(pagerState) {
                derivedStateOf {
                    (pagerState.currentPage + 1).toFloat() / OnboardingPagesCount
                }
            }
            val fabState = when (pagerState.currentPage) {
                0 -> {
                    if (onboardingState.selectedGamingGenres.isNotEmpty()) OnboardingFABState.Continue else OnboardingFABState.Skip
                }
                1 -> {
                    if (onboardingState.favouriteGames.isNotEmpty()) OnboardingFABState.Continue else OnboardingFABState.Skip
                }
                else -> {
                    if (onboardingState.followedNewsDataSources.isNotEmpty()) OnboardingFABState.Finish else OnboardingFABState.Skip
                }
            }
            val scope = rememberCoroutineScope()
            OnboardingBottomBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(NavigationBarDefaults.windowInsets),
                indicatorProgress = indicatorProgress,
                fabState = fabState,
                showForwardIcon = pagerState.currentPage != OnboardingPagesCount - 1,
                onFABClicked = { onboardingFABState ->
                    scope.launch {
                        when (onboardingFABState) {
                            OnboardingFABState.Skip -> onSkipButtonClicked()
                            OnboardingFABState.Continue -> pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            OnboardingFABState.Finish -> onFinishButtonClicked()
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(
                    ScaffoldDefaults.contentWindowInsets.exclude(
                        NavigationBarDefaults.windowInsets
                    )
                )
                .fillMaxSize()
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex: Int ->
                    val pageModifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()

                    when (pageIndex) {
                        0 -> {
                            AnimatedVisibility(
                                visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
                                enter = slideInHorizontally { it } + fadeIn(),
                                exit = slideOutHorizontally { -it } + fadeOut(),
                                modifier = pageModifier
                            ) {
                                GenresPage(
                                    allGenres = onboardingState.allGamingGenres,
                                    selectedGenres = onboardingState.selectedGamingGenres,
                                    onSelectingGenre = onSelectingGenre,
                                    onUnselectingGenre = onUnselectingGenre,
                                    onRefresh = onRefreshingGenres,
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        1 -> {
                            AnimatedVisibility(
                                visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
                                enter = slideInHorizontally { it } + fadeIn(),
                                exit = slideOutHorizontally { -it } + fadeOut(),
                                modifier = pageModifier
                            ) {
                                SelectingFavouriteGamesPage(
                                    searchQueryText = searchQuery,
                                    onUpdatingSearchQueryText = onUpdatingSearchQueryText,
                                    allGames = onboardingState.onboardingGames,
                                    favouriteUserGames = onboardingState.favouriteGames,
                                    onAddingGameToFavourites = onAddingGameToFavourites,
                                    onRemovingGameFromFavourites = onRemovingGameFromFavourites,
                                    refreshSignal = refresh,
                                    onRefresh = onRefreshingGames,
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        2 -> {
                            AnimatedVisibility(
                                visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
                                enter = slideInHorizontally { it } + fadeIn(),
                                exit = slideOutHorizontally { -it } + fadeOut(),
                                modifier = pageModifier
                            ) {
                                NewsSourcesPage(
                                    allNewsDataSources = onboardingState.allNewsDataSources,
                                    selectedNewsSources = onboardingState.followedNewsDataSources,
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    onToggleNewsSourceTile = {
                                        if (it in onboardingState.followedNewsDataSources) {
                                            onUnselectNewsDataSource(it)
                                        } else {
                                            onSelectingNewsDataSource(it)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
            if (pagerState.currentPage != 2) {
                PullRefreshIndicator(
                    refreshing = when (pagerState.currentPage) {
                        0 -> onboardingState.isRefreshingGenres
                        1 -> onboardingState.isRefreshingGames
                        else -> false
                    },
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

private val FloatAnimatableSaver = Saver<Animatable<Float, AnimationVector1D>, Float>(
    save = { it.value },
    restore = { Animatable(it) }
)

@Composable
fun OnboardingBottomBar(
    indicatorProgress: Float,
    fabState: OnboardingFABState,
    modifier: Modifier = Modifier,
    indicatorDepth: Dp = 3.dp,
    showForwardIcon: Boolean = fabState != OnboardingFABState.Finish,
    onFABClicked: (OnboardingFABState) -> Unit
) {
    val progressAnimatable = rememberSaveable(Unit, saver = FloatAnimatableSaver) { Animatable(indicatorProgress) }
    LaunchedEffect(indicatorProgress) {
        progressAnimatable.animateTo(indicatorProgress)
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val indicatorColor = MaterialTheme.colorScheme.primary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(indicatorDepth)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawLine(
                            color = indicatorColor,
                            start = Offset(0f, 0f),
                            end = Offset(progressAnimatable.value * size.width, 0f),
                            strokeWidth = indicatorDepth.toPx()
                        )
                    }
                }
                .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f))
        )
        AnimatedExtendedFab(
            targetState = fabState,
            showIcon = showForwardIcon,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.End),
            onClick = onFABClicked
        )
    }
}

@Composable
fun AnimatedExtendedFab(
    targetState: OnboardingFABState,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    onClick: (OnboardingFABState) -> Unit
) {
    var currentState by rememberSaveable(Unit) { mutableStateOf(targetState) }
    LaunchedEffect(targetState) {
        currentState = targetState
    }
    val strings = LocalStrings.current
    ExtendedFloatingActionButton(
        onClick = { onClick(currentState) },
        modifier = modifier.clearAndSetSemantics {
            stateDescription = when (currentState) {
                OnboardingFABState.Skip -> strings.on_boarding_fab_state_skip_state_desc
                OnboardingFABState.Continue -> strings.on_boarding_fab_state_continue_state_desc
                OnboardingFABState.Finish -> strings.on_boarding_fab_state_finish_state_desc
            }
        },
        shape = RoundedCornerShape(50),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        AnimatedContent(
            targetState = currentState,
            transitionSpec = {
                (slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it * 2 } + fadeOut()).using(SizeTransform(clip = false))
            },
            label = "AnimatedFABPreview"
        ) { currState ->
            val label = when (currState) {
                OnboardingFABState.Skip -> strings.on_boarding_fab_state_skip
                OnboardingFABState.Continue -> strings.on_boarding_fab_state_continue
                OnboardingFABState.Finish -> strings.on_boarding_fab_state_finish
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
                if (showIcon) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

enum class OnboardingFABState {
    Skip,
    Continue,
    Finish
}
