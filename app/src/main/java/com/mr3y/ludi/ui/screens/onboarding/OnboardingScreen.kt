package com.mr3y.ludi.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
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
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.presenter.OnBoardingViewModel
import com.mr3y.ludi.ui.presenter.model.*
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
    viewModel: OnBoardingViewModel = hiltViewModel(),
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
        onRemovingGameFromFavourites = viewModel::removeGameFromFavourites
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
            val fabState = when(pagerState.currentPage) {
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
            Banner(
                modifier = Modifier.fillMaxWidth(),
                drawableIds = onboardingState.bannerDrawablesIds,
            )
            HorizontalPager(
                pageCount = OnboardingPagesCount,
                state = pagerState,
                contentPadding = PaddingValues(16.dp),
                pageSpacing = 32.dp,
                userScrollEnabled = false,
            ) { pageIndex: Int ->
                AnimatedContent(
                    targetState = pageIndex,
                    transitionSpec = { (slideInHorizontally { it } + fadeIn()) with (slideOutHorizontally { -it } + fadeOut()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                ) { targetIndex ->
                    when(targetIndex) {
                        0 -> NewsSourcesPage(
                            allNewsDataSources = onboardingState.allNewsDataSources,
                            selectedNewsSources = onboardingState.followedNewsDataSources,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            onToggleNewsSourceTile = {
                                if (it in onboardingState.followedNewsDataSources)
                                    onUnselectNewsDataSource(it)
                                else
                                    onSelectingNewsDataSource(it)
                            },
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

@Composable
fun Banner(
    drawableIds: List<Int>,
    modifier: Modifier = Modifier
) {
    AnimatedImages(
        drawablesResIds = rememberSaveable(Unit) { drawableIds.shuffled() },
        delayTimeInMs = 2000L,
        intervalTimeInMs = 2000L,
        modifier = modifier
            .fillMaxWidth()
            .height(192.dp),
        contentScale = ContentScale.FillBounds,
        repeatMode = RepeatMode.Reverse
    )
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class
)
@Composable
fun SelectingFavouriteGamesPage(
    searchQueryText: String,
    onUpdatingSearchQueryText: (String) -> Unit,
    allGames: OnboardingGames,
    favouriteUserGames: List<FavouriteGame>,
    onAddingGameToFavourites: (FavouriteGame) -> Unit,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
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
            text = "Tell us about your favourite games",
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
                        modifier = Modifier.fillMaxHeight(),
                    )
                }
            },
            trailingIcon = if (searchQueryText.isNotEmpty()) { {
                IconButton(
                    onClick = { onUpdatingSearchQueryText("") }
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Close),
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight(),
                    )
                }
            } } else null,
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                                    if (it in favouriteUserGames)
                                        onRemovingGameFromFavourites(it)
                                    else
                                        onAddingGameToFavourites(it)
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
                    style = MaterialTheme.typography.labelLarge,
                )
                if (showIcon) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
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
                        text = if(rating == "0.0") "N/A" else rating,
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedImages(
    drawablesResIds: List<Int>,
    delayTimeInMs: Long,
    intervalTimeInMs: Long,
    modifier: Modifier = Modifier,
    repeatMode: RepeatMode? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    var currentDrawableIndex by rememberSaveable(drawablesResIds) { mutableStateOf(0) }
    var isAscendant by rememberSaveable(drawablesResIds) { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(delayTimeInMs)
        while (isActive) {
            if (currentDrawableIndex == drawablesResIds.lastIndex) {
                currentDrawableIndex = when(repeatMode) {
                    null -> break
                    RepeatMode.Restart -> 0
                    RepeatMode.Reverse -> {
                        isAscendant = false
                        drawablesResIds.lastIndex - 1
                    }
                }
            } else {
                when {
                    repeatMode == RepeatMode.Reverse && !isAscendant && currentDrawableIndex == 0 -> {
                        isAscendant = true
                        currentDrawableIndex++
                    }
                    repeatMode == RepeatMode.Reverse && !isAscendant -> currentDrawableIndex--
                    else -> currentDrawableIndex++
                }
            }
            delay(intervalTimeInMs)
        }
    }
    AnimatedContent(
        targetState = currentDrawableIndex,
        modifier = modifier,
        transitionSpec = {
            ContentTransform(
                // The initial content should stay until the target content is completely opaque.
                initialContentExit = fadeOut(animationSpec = snap(delayMillis = 300)),
                // The target content fades in. This is shown on top of the initial content.
                targetContentEnter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        // LinearOutSlowInEasing is suitable for incoming elements.
                        easing = LinearOutSlowInEasing
                    )
                )
            )
        }
    ) { drawableIndex ->
        Image(
            painter = painterResource(id = drawablesResIds[drawableIndex]),
            contentDescription = null,
            contentScale = contentScale,
        )
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
                NewsDataSource("Tech Radar", R.drawable.tech_radar_logo, Source.TechRadar),
            ),
            followedNewsDataSources = emptyList(),
            isUpdatingFollowedNewsDataSources = false,
            searchQuery = "",
            onboardingGames = OnboardingGames.SuggestedGames(Result.Success(listOf(ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder))),
            favouriteGames = emptyList(),
            isUpdatingFavouriteGames = false
        )
        OnboardingScreen(
            onboardingState = InitialOnboardingState,
            onSkipButtonClicked = {},
            onFinishButtonClicked = {},
            onSelectingNewsDataSource = {},
            onUnselectNewsDataSource = {},
            onUpdatingSearchQueryText = {},
            onAddingGameToFavourites = {},
            onRemovingGameFromFavourites = {}
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

@Preview
@Composable
fun AnimatedImagePreview() {
    LudiTheme {
        val games = listOf(
            listOf(R.drawable.game1, R.drawable.game2, R.drawable.game3, R.drawable.game4, R.drawable.game5),
            listOf(R.drawable.game6, R.drawable.game7, R.drawable.game8, R.drawable.game9, R.drawable.game10),
            listOf(R.drawable.game11, R.drawable.game12, R.drawable.game13, R.drawable.game14, R.drawable.game15),
            listOf(R.drawable.game16, R.drawable.game17, R.drawable.game18, R.drawable.game19, R.drawable.game20),
        )
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            repeat(4) { i ->
                AnimatedImages(
                    drawablesResIds = games[i],
                    delayTimeInMs = 2000L + i * 50,
                    intervalTimeInMs = 2000L,
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    contentScale = ContentScale.FillBounds,
                    repeatMode = RepeatMode.Restart
                )
            }
        }
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