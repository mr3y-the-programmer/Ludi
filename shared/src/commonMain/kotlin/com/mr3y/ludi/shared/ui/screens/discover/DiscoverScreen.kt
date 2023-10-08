package com.mr3y.ludi.shared.ui.screens.discover

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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.di.getScreenModel
import com.mr3y.ludi.shared.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.shared.ui.components.LudiErrorBox
import com.mr3y.ludi.shared.ui.components.LudiSectionHeader
import com.mr3y.ludi.shared.ui.navigation.BottomBarTab
import com.mr3y.ludi.shared.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverState
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.shared.ui.presenter.model.Platform
import com.mr3y.ludi.shared.ui.presenter.model.Store
import com.mr3y.ludi.shared.ui.presenter.model.Tag
import com.mr3y.ludi.shared.ui.presenter.model.TaggedGames
import com.mr3y.ludi.shared.ui.presenter.usecases.utils.groupByGenre
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
expect fun DiscoverScreen(modifier: Modifier = Modifier, viewModel: DiscoverViewModel)

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
    onOpenUrl: (String) -> Unit,
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
                            onOpenUrl = onOpenUrl,
                            onReachingBottomOfTheList = onReachingBottomOfTheSuggestionsList
                        )
                    }
                    else -> {
                        discoverState.gamesState as DiscoverStateGames.SearchQueryBasedGames
                        AnimatedNoInternetBanner()
                        SearchQueryAndFilterPage(
                            searchResult = discoverState.gamesState.games,
                            onOpenUrl = onOpenUrl
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
    onOpenUrl: (url: String) -> Unit,
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
                    onOpenUrl = onOpenUrl,
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
    onOpenUrl: (url: String) -> Unit,
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
                                    onOpenUrl = onOpenUrl,
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
    onOpenUrl: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    showGenre: Boolean = false
) {
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
                isHighlighted = index == highlightedItem,
                onOpenUrl = onOpenUrl,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .width(264.dp)
            )
        } else {
            GameCard(
                game = game,
                onOpenUrl = onOpenUrl,
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
