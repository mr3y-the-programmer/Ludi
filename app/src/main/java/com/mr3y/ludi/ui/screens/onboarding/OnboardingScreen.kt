package com.mr3y.ludi.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.placeholder
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.presenter.OnBoardingViewModel
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.ui.presenter.model.OnboardingState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.screens.discover.richInfoGamesSamples
import com.mr3y.ludi.ui.theme.LudiTheme
import com.mr3y.ludi.ui.theme.dark_star
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

const val OnboardingPagesCount = 3

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnBoardingViewModel = hiltViewModel()
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
        onSelectingGenre = viewModel::selectGenre,
        onUnselectingGenre = viewModel::unselectGenre
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onboardingState: OnboardingState,
    modifier: Modifier = Modifier,
    onSkipButtonClicked: () -> Unit,
    onFinishButtonClicked: () -> Unit,
    onSelectingNewsDataSource: (NewsDataSource) -> Unit,
    onUnselectNewsDataSource: (NewsDataSource) -> Unit,
    onUpdatingSearchQueryText: (String) -> Unit,
    onAddingGameToFavourites: (FavouriteGame) -> Unit,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
    onSelectingGenre: (GameGenre) -> Unit,
    onUnselectingGenre: (GameGenre) -> Unit
) {
    val pagerState = rememberPagerState()
    Scaffold(
        modifier = modifier
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
                modifier = Modifier.fillMaxWidth(),
                indicatorProgress = indicatorProgress,
                fabState = fabState,
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
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            HorizontalPager(
                pageCount = OnboardingPagesCount,
                state = pagerState,
                contentPadding = PaddingValues(16.dp),
                pageSpacing = 32.dp,
                userScrollEnabled = false
            ) { pageIndex: Int ->
                AnimatedContent(
                    targetState = pageIndex,
                    transitionSpec = { (slideInHorizontally { it } + fadeIn()) with (slideOutHorizontally { -it } + fadeOut()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                ) { targetIndex ->
                    when (targetIndex) {
                        0 -> GenresPage(
                            allGenres = onboardingState.allGamingGenres,
                            selectedGenres = onboardingState.selectedGamingGenres,
                            onSelectingGenre = onSelectingGenre,
                            onUnselectingGenre = onUnselectingGenre,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        )
                        1 -> SelectingFavouriteGamesPage(
                            searchQueryText = onboardingState.searchQuery,
                            onUpdatingSearchQueryText = onUpdatingSearchQueryText,
                            allGames = onboardingState.onboardingGames,
                            favouriteUserGames = onboardingState.favouriteGames,
                            onAddingGameToFavourites = onAddingGameToFavourites,
                            onRemovingGameFromFavourites = onRemovingGameFromFavourites,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            scrollState = scrollState
                        )
                        2 -> NewsSourcesPage(
                            allNewsDataSources = onboardingState.allNewsDataSources,
                            selectedNewsSources = onboardingState.followedNewsDataSources,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            onToggleNewsSourceTile = {
                                if (it in onboardingState.followedNewsDataSources) {
                                    onUnselectNewsDataSource(it)
                                } else {
                                    onSelectingNewsDataSource(it)
                                }
                            }
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = onboardingState.isUpdatingFollowedNewsDataSources || onboardingState.isUpdatingFavouriteGames,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Saving...",
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
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
            showIcon = fabState != OnboardingFABState.Finish,
            label = { it.name.replaceFirstChar { char -> char.uppercase() } },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.End),
            onClick = onFABClicked
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedExtendedFab(
    targetState: OnboardingFABState,
    label: (OnboardingFABState) -> String,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    onClick: (OnboardingFABState) -> Unit
) {
    var currentState by rememberSaveable(Unit) { mutableStateOf(targetState) }
    LaunchedEffect(targetState) {
        currentState = targetState
    }
    ExtendedFloatingActionButton(
        onClick = { onClick(currentState) },
        modifier = modifier,
        shape = RoundedCornerShape(50),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        AnimatedContent(
            targetState = targetState,
            transitionSpec = {
                (slideInVertically { -it } + fadeIn() with slideOutVertically { it * 2 } + fadeOut()).using(SizeTransform(clip = false))
            }
        ) { currentState ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = label(currentState),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )
                if (showIcon) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
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

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    LudiTheme {
        OnboardingScreen(
            onboardingState = PreviewOnboardingState,
            onSkipButtonClicked = {},
            onFinishButtonClicked = {},
            onSelectingNewsDataSource = {},
            onUnselectNewsDataSource = {},
            onUpdatingSearchQueryText = {},
            onAddingGameToFavourites = {},
            onRemovingGameFromFavourites = {},
            onSelectingGenre = {},
            onUnselectingGenre = {},
        )
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun OnboardingBottomBarPreview() {
    LudiTheme {
        var progress by remember { mutableStateOf(0.5f) }
        var fabState by remember { mutableStateOf(OnboardingFABState.Skip) }
        LaunchedEffect(Unit) {
            delay(3000)
            progress *= 2
        }
        OnboardingBottomBar(
            modifier = Modifier.padding(8.dp),
            indicatorProgress = progress,
            fabState = fabState,
            onFABClicked = {
                fabState = when (it) {
                    OnboardingFABState.Skip -> OnboardingFABState.Continue
                    OnboardingFABState.Continue -> OnboardingFABState.Finish
                    OnboardingFABState.Finish -> OnboardingFABState.Skip
                }
            }
        )
    }
}
