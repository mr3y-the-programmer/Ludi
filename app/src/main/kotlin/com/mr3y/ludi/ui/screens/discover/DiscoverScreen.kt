package com.mr3y.ludi.ui.screens.discover

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getScreenModel
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.components.LudiSectionHeader
import com.mr3y.ludi.ui.navigation.BottomBarTab
import com.mr3y.ludi.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.ui.presenter.model.DiscoverState
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.Platform
import com.mr3y.ludi.ui.presenter.model.Store
import com.mr3y.ludi.ui.presenter.model.Tag
import com.mr3y.ludi.ui.presenter.model.TaggedGames
import com.mr3y.ludi.ui.presenter.usecases.utils.groupByGenre
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

object DiscoverScreenTab : Screen, BottomBarTab {
    override val key: ScreenKey
        get() = "discover"

    override val label: String
        get() = "Discover"
    override val icon: ImageVector
        get() = Icons.Outlined.Search

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<DiscoverViewModel>()
        DiscoverScreen(viewModel = screenModel)
    }
}

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel
) {
    val discoverState by viewModel.discoverState.collectAsStateWithLifecycle()
    DiscoverScreen(
        discoverState = discoverState,
        onUpdatingSearchQueryText = viewModel::updateSearchQuery,
        onSelectingPlatform = viewModel::addToSelectedPlatforms,
        onUnselectingPlatform = viewModel::removeFromSelectedPlatforms,
        onSelectingStore = viewModel::addToSelectedStores,
        onUnselectingStore = viewModel::removeFromSelectedStores,
        onSelectingTag = viewModel::addToSelectedTags,
        onUnselectingTag = viewModel::removeFromSelectedTags,
        onReachingBottomOfTheSuggestionsList = viewModel::loadNewSuggestedGames,
        onRefresh = viewModel::refresh,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DiscoverScreen(
    discoverState: DiscoverState,
    onUpdatingSearchQueryText: (String) -> Unit,
    onSelectingPlatform: (Platform) -> Unit,
    onUnselectingPlatform: (Platform) -> Unit,
    onSelectingStore: (Store) -> Unit,
    onUnselectingStore: (Store) -> Unit,
    onSelectingTag: (Tag) -> Unit,
    onUnselectingTag: (Tag) -> Unit,
    onReachingBottomOfTheSuggestionsList: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openFiltersSheet by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val refreshState = rememberPullRefreshState(
        refreshing = discoverState.isRefreshing,
        onRefresh = onRefresh
    )
    Scaffold(
        modifier = modifier
            .pullRefresh(refreshState)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DiscoverTopBar(
                searchQuery = discoverState.searchQuery,
                onSearchQueryValueChanged = onUpdatingSearchQueryText,
                onCloseClicked = { openFiltersSheet = !openFiltersSheet },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { contentPadding ->
        Box(
            modifier = Modifier.padding(contentPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (discoverState.gamesState) {
                    is DiscoverStateGames.SuggestedGames -> {
                        AnimatedNoInternetBanner()
                        SuggestedGamesPage(
                            suggestedGames = discoverState.gamesState,
                            onReachingBottomOfTheList = onReachingBottomOfTheSuggestionsList
                        )
                    }
                    else -> {
                        discoverState.gamesState as DiscoverStateGames.SearchQueryBasedGames
                        AnimatedNoInternetBanner()
                        SearchQueryAndFilterPage(
                            searchResult = discoverState.gamesState.games
                        )
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = discoverState.isRefreshing,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
    if (openFiltersSheet) {
        FiltersBottomSheet(
            filtersState = discoverState.filtersState,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { openFiltersSheet = false },
            onCloseClicked = { openFiltersSheet = false },
            onSelectingPlatform,
            onUnselectingPlatform,
            onSelectingStore,
            onUnselectingStore,
            onSelectingTag,
            onUnselectingTag
        )
    }
}

@Composable
fun SuggestedGamesPage(
    suggestedGames: DiscoverStateGames.SuggestedGames,
    onReachingBottomOfTheList: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var isNewDataBeingLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(isNewDataBeingLoaded) {
        // Avoid displaying loading indicator indefinitely
        if (isNewDataBeingLoaded) {
            delay(5_000L)
            isNewDataBeingLoaded = false
        }
    }
    LaunchedEffect(listState) {
        snapshotFlow {
            val lastItem = listState.layoutInfo.visibleItemsInfo.last()

            lastItem.index == listState.layoutInfo.totalItemsCount - 1 &&
                lastItem.offset + lastItem.size == listState.layoutInfo.viewportEndOffset
        }.distinctUntilChanged()
            .collect { hasReachedTheEnd ->
                if (hasReachedTheEnd) {
                    isNewDataBeingLoaded = true
                    onReachingBottomOfTheList()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        suggestedGames.taggedGamesList.forEachIndexed { index, taggedGames ->
            val label = getLabelFor(taggedGames)
            item {
                LudiSectionHeader(
                    text = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
            item {
                RichInfoGamesSection(
                    games = taggedGames.games,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp),
                    isTrendingGame = taggedGames is TaggedGames.TrendingGames,
                    showGenre = true
                )
            }
            if (isNewDataBeingLoaded && index == suggestedGames.taggedGamesList.lastIndex) {
                item {
                    Box(
                        modifier = modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

private fun getLabelFor(taggedGames: TaggedGames): String {
    return when (taggedGames) {
        is TaggedGames.TrendingGames -> "Trending"
        is TaggedGames.TopRatedGames -> "Top rated"
        is TaggedGames.MultiplayerGames -> "Multiplayer"
        is TaggedGames.BasedOnFavGenresGames -> "You might also like"
        is TaggedGames.FreeGames -> "Free to play"
        is TaggedGames.StoryGames -> "Story based"
        is TaggedGames.BoardGames -> "Board"
        is TaggedGames.ESportsGames -> "ESports"
        is TaggedGames.RaceGames -> "Race"
        is TaggedGames.PuzzleGames -> "Puzzle"
        is TaggedGames.SoundtrackGames -> "SoundTrack"
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchQueryAndFilterPage(
    searchResult: Result<Map<GameGenre, List<Game>>, Throwable>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (searchResult) {
            is Result.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is Result.Success -> {
                searchResult.data.forEach { (gameGenre, games) ->
                    item {
                        LudiSectionHeader(
                            text = gameGenre.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )
                    }
                    item {
                        val state = rememberLazyListState()
                        LazyRow(
                            state = state,
                            flingBehavior = rememberSnapFlingBehavior(state),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(games) {
                                GameCard(
                                    game = it,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .width(200.dp)
                                        .height(280.dp)
                                )
                            }
                        }
                    }
                }
            }
            is Result.Error -> {
                item {
                    LudiErrorBox(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun RichInfoGamesSection(
    games: Result<List<Game>, Throwable>,
    isTrendingGame: Boolean,
    modifier: Modifier = Modifier,
    showGenre: Boolean = false
) {
    val dominantColorsState = rememberDominantColorsState()
    val listState = rememberLazyListState()
    var highlightedItem by remember { mutableIntStateOf(0) }
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            layoutInfo.visibleItemsInfo
                .firstOrNull { it.offset >= layoutInfo.viewportStartOffset }
                ?.index ?: 0
        }.distinctUntilChanged()
            .collect {
                highlightedItem = it
            }
    }
    GamesSectionScaffold(
        gamesResult = games,
        state = listState,
        modifier = modifier
    ) { index, game ->
        if (isTrendingGame) {
            TrendingGameCard(
                game = game,
                dominantColorsState = dominantColorsState,
                isHighlighted = index == highlightedItem,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .width(264.dp)
            )
        } else {
            GameCard(
                game = game,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .width(200.dp)
                    .height(280.dp),
                showGenre = showGenre
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> GamesSectionScaffold(
    gamesResult: Result<List<T>, Throwable>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    itemContent: @Composable (index: Int, T?) -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        flingBehavior = rememberSnapFlingBehavior(state),
        horizontalArrangement = horizontalArrangement
    ) {
        when (gamesResult) {
            is Result.Loading -> {
                items(10) { index ->
                    itemContent(index, null)
                }
            }
            is Result.Success -> {
                itemsIndexed(gamesResult.data) { index, game ->
                    itemContent(index, game)
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

@LudiPreview
@Composable
fun DiscoverScreenPreview() {
    LudiTheme {
        var initialList by remember {
            mutableStateOf(
                listOf(
                    TaggedGames.TrendingGames(Result.Success(gamesSamples)),
                    TaggedGames.TopRatedGames(Result.Success(gamesSamples)),
                    TaggedGames.MultiplayerGames(Result.Success(gamesSamples))
                )
            )
        }
        var loadNewGames by remember { mutableStateOf(false) }
        LaunchedEffect(loadNewGames) {
            if (loadNewGames) {
                delay(1000)
                initialList = initialList + listOf(
                    TaggedGames.FreeGames(Result.Success(gamesSamples)),
                    TaggedGames.BasedOnFavGenresGames(Result.Success(gamesSamples))
                )
            }
        }
        DiscoverScreen(
            discoverState = DiscoverState(
                searchQuery = "",
                gamesState = DiscoverStateGames.SuggestedGames(taggedGamesList = initialList),
                filtersState = DiscoverViewModel.InitialFiltersState,
                isRefreshing = false
            ),
            onUpdatingSearchQueryText = {},
            onSelectingPlatform = {},
            onUnselectingPlatform = {},
            onSelectingStore = {},
            onUnselectingStore = {},
            onSelectingTag = {},
            onUnselectingTag = {},
            onReachingBottomOfTheSuggestionsList = { loadNewGames = true },
            onRefresh = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@LudiPreview
@Composable
fun SearchResultsPagePreview() {
    LudiTheme {
        SearchQueryAndFilterPage(
            searchResult = Result.Success(gamesSamples.groupByGenre()),
            modifier = Modifier.fillMaxSize()
        )
    }
}
