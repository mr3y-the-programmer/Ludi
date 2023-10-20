package com.mr3y.ludi.shared.ui.screens.discover

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.runtime.SideEffect
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
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.mr3y.ludi.shared.core.model.Game
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
    searchQuery: String,
    onUpdatingSearchQueryText: (String) -> Unit,
    onSelectingPlatform: (Platform) -> Unit,
    onUnselectingPlatform: (Platform) -> Unit,
    onSelectingStore: (Store) -> Unit,
    onUnselectingStore: (Store) -> Unit,
    onSelectingTag: (Tag) -> Unit,
    onUnselectingTag: (Tag) -> Unit,
    onRefresh: () -> Unit,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var openFilters by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var refresh by rememberSaveable(Unit) { mutableIntStateOf(0) }
    val refreshState = rememberPullRefreshState(
        refreshing = discoverState.isRefreshing,
        onRefresh = {
            onRefresh() // notify ViewModel to update state
            refresh++
        }
    )
    Scaffold(
        modifier = modifier
            .pullRefresh(refreshState)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DiscoverTopBar(
                searchQuery = searchQuery,
                onSearchQueryValueChanged = onUpdatingSearchQueryText,
                onTuneClicked = { openFilters = !openFilters },
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
                            refreshSignal = refresh
                        )
                    }
                    else -> {
                        discoverState.gamesState as DiscoverStateGames.SearchQueryBasedGames
                        AnimatedNoInternetBanner()
                        SearchQueryAndFilterPage(
                            searchResultsGames = discoverState.gamesState,
                            onOpenUrl = onOpenUrl,
                            refreshSignal = refresh
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
    if (openFilters) {
        Filters(
            discoverState.filtersState,
            onDismiss = { openFilters = false },
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
    onOpenUrl: (url: String) -> Unit,
    refreshSignal: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            LudiSectionHeader(
                text = "Trending",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.trendingGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = true,
                showGenre = true
            )
        }
        item {
            LudiSectionHeader(
                text = "Top rated",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.topRatedGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
        item {
            LudiSectionHeader(
                text = "Multiplayer",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.multiplayerGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
        if (suggestedGames.favGenresBasedGames != null) {
            item {
                LudiSectionHeader(
                    text = "You might also like",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
            item {
                RichInfoGamesSection(
                    games = suggestedGames.favGenresBasedGames.collectAsLazyPagingItems(),
                    onOpenUrl = onOpenUrl,
                    refreshSignal = refreshSignal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp),
                    isTrendingGame = false,
                    showGenre = true
                )
            }
        }
        item {
            LudiSectionHeader(
                text = "Free to play",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.freeGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
        item {
            LudiSectionHeader(
                text = "Story based",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.storyGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
        item {
            LudiSectionHeader(
                text = "Board",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.boardGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
        item {
            LudiSectionHeader(
                text = "ESports",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.eSportsGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
        item {
            LudiSectionHeader(
                text = "Race",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.raceGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
        item {
            LudiSectionHeader(
                text = "Puzzle",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.puzzleGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
        item {
            LudiSectionHeader(
                text = "SoundTrack",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.soundtrackGames.collectAsLazyPagingItems(),
                onOpenUrl = onOpenUrl,
                refreshSignal = refreshSignal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp),
                isTrendingGame = false,
                showGenre = true
            )
        }
    }
}

@Composable
fun SearchQueryAndFilterPage(
    searchResultsGames: DiscoverStateGames.SearchQueryBasedGames,
    onOpenUrl: (url: String) -> Unit,
    refreshSignal: Int,
    modifier: Modifier = Modifier
) {
    val games = searchResultsGames.games.collectAsLazyPagingItems()
    LaunchedEffect(refreshSignal) {
        if (refreshSignal > 0) {
            games.refresh()
        }
    }
    SideEffect {
        if (games.loadState.refresh is LoadStateError || games.loadState.append is LoadStateError) {
            games.retry()
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        if (games.loadState.refresh is LoadStateLoading) {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (games.loadState.refresh is LoadStateNotLoading) {
            items(
                count = games.itemCount,
                key = games.itemKey { it.id },
                contentType = games.itemContentType { it }
            ) { index ->
                GameCard(
                    game = games[index],
                    onOpenUrl = onOpenUrl,
                    showGenre = true,
                    modifier = Modifier
                        .width(90.dp)
                        .height(280.dp)
                )
            }
        }

        if (games.loadState.refresh is LoadStateError) {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }

        if (games.loadState.append is LoadStateLoading) {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (games.loadState.append is LoadStateError) {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun RichInfoGamesSection(
    games: LazyPagingItems<Game>,
    isTrendingGame: Boolean,
    onOpenUrl: (url: String) -> Unit,
    refreshSignal: Int,
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
    LaunchedEffect(refreshSignal) {
        if (refreshSignal > 0) {
            games.refresh()
        }
    }
    SideEffect {
        if (games.loadState.refresh is LoadStateError || games.loadState.append is LoadStateError) {
            games.retry()
        }
    }
    GamesSectionScaffold(
        games = games,
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
fun GamesSectionScaffold(
    games: LazyPagingItems<Game>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    itemContent: @Composable (index: Int, Game?) -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        flingBehavior = rememberSnapFlingBehavior(state),
        horizontalArrangement = horizontalArrangement
    ) {
        if (games.loadState.refresh is LoadStateLoading) {
            items(10) { index ->
                itemContent(index, null)
            }
        }

        if (games.loadState.refresh is LoadStateNotLoading) {
            items(
                count = games.itemCount,
                key = games.itemKey { it.id },
                contentType = games.itemContentType { it }
            ) { index ->
                itemContent(index, games[index])
            }
        }

        if (games.loadState.refresh is LoadStateError) {
            item {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }

        if (games.loadState.append is LoadStateLoading) {
            items(10) { index ->
                itemContent(index, null)
            }
        }

        if (games.loadState.append is LoadStateError) {
            item {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
