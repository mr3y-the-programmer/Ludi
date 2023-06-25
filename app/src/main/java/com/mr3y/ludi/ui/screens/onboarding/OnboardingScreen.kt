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

const val OnboardingPagesCount = 2

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
                    if (onboardingState.followedNewsDataSources.isNotEmpty()) OnboardingFABState.Continue else OnboardingFABState.Skip
                }
                else -> {
                    if (onboardingState.favouriteGames.isNotEmpty()) OnboardingFABState.Finish else OnboardingFABState.Skip
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
                            OnboardingFABState.Continue -> pagerState.animateScrollToPage(1)
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
                        0 -> NewsSourcesPage(
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
                        1 -> SelectingFavouriteGamesPage(
                            searchQueryText = onboardingState.searchQuery,
                            onUpdatingSearchQueryText = onUpdatingSearchQueryText,
                            allGames = onboardingState.onboardingGames,
                            favouriteUserGames = onboardingState.favouriteGames,
                            onAddingGameToFavourites = onAddingGameToFavourites,
                            onRemovingGameFromFavourites = onRemovingGameFromFavourites,
                            allGenres = onboardingState.allGamingGenres,
                            selectedGenres = onboardingState.selectedGamingGenres,
                            onSelectingGenre = onSelectingGenre,
                            onUnselectingGenre = onUnselectingGenre,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            scrollState = scrollState
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
        val indicatorHeightInPx = with(LocalDensity.current) { indicatorDepth.toPx() }
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
                            strokeWidth = indicatorHeightInPx
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsSourcesPage(
    allNewsDataSources: List<NewsDataSource>,
    selectedNewsSources: List<NewsDataSource>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onToggleNewsSourceTile: (NewsDataSource) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Column(
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Follow your favorite news sources",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.align(Alignment.End),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "You can always change that later",
                modifier = Modifier.align(Alignment.End),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allNewsDataSources.forEach {
                NewsSourceTile(
                    newsDataSource = it,
                    selected = it in selectedNewsSources,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(IntrinsicSize.Max),
                    onToggleSelection = onToggleNewsSourceTile
                )
            }
        }
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class,
)
@Composable
fun SelectingFavouriteGamesPage(
    searchQueryText: String,
    onUpdatingSearchQueryText: (String) -> Unit,
    allGames: OnboardingGames,
    favouriteUserGames: List<FavouriteGame>,
    onAddingGameToFavourites: (FavouriteGame) -> Unit,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
    allGenres: Result<ResourceWrapper<Set<GameGenre>>, Throwable>,
    selectedGenres: Set<GameGenre>,
    onSelectingGenre: (GameGenre) -> Unit,
    onUnselectingGenre: (GameGenre) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        val softwareKeyboard = LocalSoftwareKeyboardController.current
        val imePadding = WindowInsets.ime.getBottom(LocalDensity.current)
        LaunchedEffect(imePadding) {
            if (imePadding > 0) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
        Text(
//            text = "Tell us about your favourite games",
            text = "What are your favourite game genres",
            modifier = Modifier.align(Alignment.End),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "You can always change that later",
            modifier = Modifier.align(Alignment.End),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
        when (allGenres) {
            is Result.Success -> {
                    if (allGenres.data is ResourceWrapper.ActualResource) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                        allGenres.data.resource.forEach { genre ->
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .border(
                                        1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        color = if (genre in selectedGenres) MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.2f
                                        ) else Color.Transparent
                                    )
                                    .selectable(
                                        selected = genre in selectedGenres,
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        role = Role.Button,
                                        onClick = {
                                            if (genre in selectedGenres)
                                                onUnselectingGenre(genre)
                                            else
                                                onSelectingGenre(genre)
                                        }
                                    )
                                    .animateContentSize()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "\n",
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = genre.name,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.width(IntrinsicSize.Min),
                                    textAlign = TextAlign.Center
                                )
                                if (genre in selectedGenres) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.padding(8.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is Result.Error -> {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }
        TextField(
            value = searchQueryText,
            onValueChange = onUpdatingSearchQueryText,
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Search),
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            },
            trailingIcon = if (searchQueryText.isNotEmpty()) {
                {
                    IconButton(
                        onClick = { onUpdatingSearchQueryText("") }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Close),
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
            } else {
                null
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { softwareKeyboard?.hide() }),
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .align(Alignment.CenterHorizontally)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
        )
        Text(
            text = "Suggestions",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start
        )
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Adaptive(minSize = 64.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(236.dp),
            horizontalItemSpacing = 8.dp,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (val result = allGames.games) {
                is Result.Success -> {
                    itemsIndexed(result.data) { index, gameWrapper ->
                        key(index) {
                            val game = gameWrapper.actualResource
                            GameTile(
                                game = game,
                                selected = game?.let { FavouriteGame(it.id, it.name, it.imageUrl, it.rating) } in favouriteUserGames,
                                onToggleSelectingFavouriteGame = {
                                    if (it in favouriteUserGames) {
                                        onRemovingGameFromFavourites(it)
                                    } else {
                                        onAddingGameToFavourites(it)
                                    }
                                },
                                modifier = Modifier.height(IntrinsicSize.Min)
                            )
                        }
                    }
                }
                is Result.Error -> {
                    item {
                        LudiErrorBox(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
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

@Composable
fun NewsSourceTile(
    newsDataSource: NewsDataSource,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onToggleSelection: (NewsDataSource) -> Unit
) {
    Card(
        modifier = modifier
            .selectable(
                selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = { onToggleSelection(newsDataSource) }
            )
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = newsDataSource.drawableId),
                contentDescription = null
            )
            Text(
                text = newsDataSource.name,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge
            )
            val unselectedColor = MaterialTheme.colorScheme.background
            val circleModifier = Modifier
                .size(20.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .clip(CircleShape)
            Box(
                modifier = circleModifier
                    .then(
                        if (selected) circleModifier else circleModifier.drawBehind { drawCircle(color = unselectedColor) }
                    ),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun GameTile(
    game: RichInfoGame?,
    selected: Boolean,
    onToggleSelectingFavouriteGame: (FavouriteGame) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = {
                    if (game != null) {
                        onToggleSelectingFavouriteGame(
                            FavouriteGame(
                                id = game.id,
                                title = game.name,
                                imageUrl = game.imageUrl,
                                rating = game.rating
                            )
                        )
                    }
                }
            )
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = game?.imageUrl,
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp)
                    .placeholder(
                        visible = game == null,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                        )
                    ),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = game?.name ?: "Name Placeholder",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    modifier = Modifier.placeholder(
                        visible = game == null,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                        )
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Star),
                        contentDescription = null,
                        tint = dark_star
                    )
                    val rating = game?.rating?.toString() ?: "Rating Placeholder"
                    Text(
                        text = if (rating == "0.0") "N/A" else rating,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.placeholder(
                            visible = game == null,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                            )
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            val unselectedColor = MaterialTheme.colorScheme.background
            val circleModifier = Modifier
                .size(20.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .clip(CircleShape)
            Box(
                modifier = circleModifier
                    .then(
                        if (selected) circleModifier else circleModifier.drawBehind { drawCircle(color = unselectedColor) }
                    ),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    LudiTheme {
        val InitialOnboardingState = OnboardingState(
            bannerDrawablesIds = listOf(
                R.drawable.game1, R.drawable.game2,
                R.drawable.game3, R.drawable.game4,
                R.drawable.game5, R.drawable.game6,
                R.drawable.game7, R.drawable.game8,
                R.drawable.game9, R.drawable.game10,
                R.drawable.game11, R.drawable.game12,
                R.drawable.game13, R.drawable.game14,
                R.drawable.game15, R.drawable.game16,
                R.drawable.game17, R.drawable.game18,
                R.drawable.game19, R.drawable.game20,
                R.drawable.game21, R.drawable.game22,
                R.drawable.game23, R.drawable.game24
            ),
            allNewsDataSources = listOf(
                NewsDataSource("Game spot", R.drawable.game_spot_logo, Source.GameSpot),
                NewsDataSource("Giant bomb", R.drawable.giant_bomb_logo, Source.GiantBomb),
                NewsDataSource("IGN", R.drawable.ign_logo, Source.IGN),
                NewsDataSource("MMO bomb", R.drawable.mmobomb_logo, Source.MMOBomb),
                NewsDataSource("Tech Radar", R.drawable.tech_radar_logo, Source.TechRadar)
            ),
            followedNewsDataSources = emptyList(),
            isUpdatingFollowedNewsDataSources = false,
            searchQuery = "",
            onboardingGames = OnboardingGames.SuggestedGames(Result.Success(listOf(ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder))),
            favouriteGames = emptyList(),
            isUpdatingFavouriteGames = false,
            allGamingGenres = Result.Success(
                ResourceWrapper.ActualResource(
                    setOf(
                        GameGenre(
                            id = 1,
                            name = "Action",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 2,
                            name = "Adventure",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 3,
                            name = "Arcade",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 4,
                            name = "Board games",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 5,
                            name = "Educational",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 6,
                            name = "Family",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 7,
                            name = "Indie",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 8,
                            name = "Massively Multiplayer",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 9,
                            name = "Racing",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                        GameGenre(
                            id = 10,
                            name = "Simulation",
                            slug = null,
                            gamesCount = 2000,
                            imageUrl = null
                        ),
                    )
                )
            ),
            selectedGamingGenres = setOf(
                GameGenre(
                    id = 3,
                    name = "Arcade",
                    slug = null,
                    gamesCount = 2000,
                    imageUrl = null
                ),
                GameGenre(
                    id = 8,
                    name = "Massively Multiplayer",
                    slug = null,
                    gamesCount = 2000,
                    imageUrl = null
                ),
                GameGenre(
                    id = 9,
                    name = "Racing",
                    slug = null,
                    gamesCount = 2000,
                    imageUrl = null
                ),
            )
        )
        OnboardingScreen(
            onboardingState = InitialOnboardingState,
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
fun GameTilePreview() {
    LudiTheme {
        GameTile(
            game = richInfoGamesSamples.first().actualResource,
            selected = false,
            onToggleSelectingFavouriteGame = {}
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
