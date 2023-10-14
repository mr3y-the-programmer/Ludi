package com.mr3y.ludi.shared.ui.screens.deals

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.di.getScreenModel
import com.mr3y.ludi.shared.ui.adaptive.LocalWindowSizeClass
import com.mr3y.ludi.shared.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.shared.ui.components.LudiErrorBox
import com.mr3y.ludi.shared.ui.navigation.BottomBarTab
import com.mr3y.ludi.shared.ui.presenter.DealsViewModel
import com.mr3y.ludi.shared.ui.presenter.model.DealStore
import com.mr3y.ludi.shared.ui.presenter.model.DealsState
import com.mr3y.ludi.shared.ui.presenter.model.GiveawayPlatform
import com.mr3y.ludi.shared.ui.presenter.model.GiveawayStore

object DealsScreenTab : Screen, BottomBarTab {

    override val key: ScreenKey
        get() = "deals"

    override val label: String
        get() = "Deals"
    override val icon: ImageVector
        get() = Icons.Outlined.LocalOffer

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<DealsViewModel>()
        DealsScreen(viewModel = screenModel)
    }
}

@Composable
expect fun DealsScreen(modifier: Modifier = Modifier, viewModel: DealsViewModel)

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun DealsScreen(
    dealsState: DealsState,
    searchQuery: String,
    modifier: Modifier = Modifier,
    onUpdateSearchQuery: (String) -> Unit,
    onSelectingDealStore: (DealStore) -> Unit,
    onUnselectingDealStore: (DealStore) -> Unit,
    onSelectingGiveawayStore: (GiveawayStore) -> Unit,
    onUnselectingGiveawayStore: (GiveawayStore) -> Unit,
    onSelectingGiveawayPlatform: (GiveawayPlatform) -> Unit,
    onUnselectingGiveawayPlatform: (GiveawayPlatform) -> Unit,
    onRefreshDeals: () -> Unit,
    onRefreshGiveaways: () -> Unit,
    onSelectTab: (index: Int) -> Unit,
    onToggleFilters: () -> Unit,
    onOpenUrl: (url: String) -> Unit
) {
    val topBarScrollState = rememberKeyedTopAppBarState(input = dealsState.selectedTab)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarScrollState)
    val sheetType = if (dealsState.selectedTab == 0) BottomSheetType.Deals else BottomSheetType.Giveaways
    val strings = LocalStrings.current
    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        val tabsLabels = listOf(strings.deals_label, strings.giveaways_label)
        SegmentedTabRow(
            numOfTabs = tabsLabels.size,
            selectedTabIndex = dealsState.selectedTab,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp)
        ) { index ->
            SegmentedTab(
                selected = index == dealsState.selectedTab,
                label = tabsLabels[index],
                onClick = { onSelectTab(index) }
            )
        }
        val refreshState = rememberPullRefreshState(
            refreshing = if (dealsState.selectedTab == 0) dealsState.isRefreshingDeals else dealsState.isRefreshingGiveaways,
            onRefresh = {
                when (dealsState.selectedTab) {
                    0 -> onRefreshDeals()
                    1 -> onRefreshGiveaways()
                }
            }
        )
        Scaffold(
            topBar = {
                SearchFilterBar(
                    searchQuery = searchQuery,
                    onSearchQueryValueChanged = onUpdateSearchQuery,
                    onFilterClicked = onToggleFilters,
                    showSearchBar = dealsState.selectedTab == 0,
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            },
            modifier = Modifier
                .pullRefresh(refreshState)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentWindowInsets = WindowInsets(0.dp)
        ) { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedNoInternetBanner()
                    val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
                    if (widthSizeClass >= WindowWidthSizeClass.Medium) {
                        val gridState = rememberKeyedLazyGridState(input = dealsState.selectedTab)
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Adaptive(192.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            if (dealsState.selectedTab == 0) {
                                sectionScaffold(
                                    result = dealsState.deals
                                ) {
                                    Deal(
                                        deal = it,
                                        onClick = {
                                            if (it != null) {
                                                onOpenUrl("https://www.cheapshark.com/redirect?dealID=${it.dealID}")
                                            }
                                        },
                                        modifier = Modifier.height(IntrinsicSize.Min)
                                    )
                                }
                            } else {
                                sectionScaffold(
                                    result = dealsState.giveaways
                                ) {
                                    GamerPowerGameGiveaway(
                                        giveaway = it,
                                        onClick = {
                                            if (it != null) {
                                                onOpenUrl(it.gamerPowerUrl)
                                            }
                                        },
                                        modifier = Modifier.height(IntrinsicSize.Min)
                                    )
                                }
                            }
                        }
                    } else {
                        val listState = rememberKeyedLazyListState(input = dealsState.selectedTab)
                        LazyColumn(
                            state = listState,
                            flingBehavior = rememberSnapFlingBehavior(listState),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (dealsState.selectedTab == 0) {
                                sectionScaffold(
                                    result = dealsState.deals
                                ) {
                                    Deal(
                                        deal = it,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .fillMaxWidth()
                                            .height(IntrinsicSize.Min),
                                        onClick = {
                                            if (it != null) {
                                                onOpenUrl("https://www.cheapshark.com/redirect?dealID=${it.dealID}")
                                            }
                                        }
                                    )
                                }
                            } else {
                                sectionScaffold(
                                    result = dealsState.giveaways
                                ) {
                                    GamerPowerGameGiveaway(
                                        giveaway = it,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .fillMaxWidth()
                                            .height(IntrinsicSize.Min),
                                        onClick = {
                                            if (it != null) {
                                                onOpenUrl(it.gamerPowerUrl)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = if (dealsState.selectedTab == 0) dealsState.isRefreshingDeals else dealsState.isRefreshingGiveaways,
                    state = refreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
    if (dealsState.showFilters) {
        Filters(
            dealsFiltersState = dealsState.dealsFiltersState,
            giveawaysFiltersState = dealsState.giveawaysFiltersState,
            type = sheetType,
            onDismissRequest = onToggleFilters,
            onCloseClicked = onToggleFilters,
            onSelectingGiveawayPlatform = onSelectingGiveawayPlatform,
            onUnselectingGiveawayPlatform = onUnselectingGiveawayPlatform,
            onSelectingDealStore = onSelectingDealStore,
            onUnselectingDealStore = onUnselectingDealStore,
            onSelectingGiveawayStore = onSelectingGiveawayStore,
            onUnselectingGiveawayStore = onUnselectingGiveawayStore
        )
    }
}

/**
 * Creates a [TopAppBarState] that is remembered across compositions.
 *
 * @param initialHeightOffsetLimit the initial value for [TopAppBarState.heightOffsetLimit],
 * which represents the pixel limit that a top app bar is allowed to collapse when the scrollable
 * content is scrolled
 * @param initialHeightOffset the initial value for [TopAppBarState.heightOffset]. The initial
 * offset height offset should be between zero and [initialHeightOffsetLimit].
 * @param initialContentOffset the initial value for [TopAppBarState.contentOffset]
 */
@ExperimentalMaterial3Api
@Composable
fun rememberKeyedTopAppBarState(
    input: Any,
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0f,
    initialContentOffset: Float = 0f
): TopAppBarState {
    return rememberSaveable(input, saver = TopAppBarState.Saver) {
        TopAppBarState(
            initialHeightOffsetLimit,
            initialHeightOffset,
            initialContentOffset
        )
    }
}

@Composable
fun rememberKeyedLazyListState(
    input: Any,
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyListState {
    return rememberSaveable(input, saver = LazyListState.Saver) {
        LazyListState(
            initialFirstVisibleItemIndex,
            initialFirstVisibleItemScrollOffset
        )
    }
}

@Composable
fun rememberKeyedLazyGridState(
    input: Any,
    firstVisibleItemIndex: Int = 0,
    firstVisibleItemScrollOffset: Int = 0
): LazyGridState {
    return rememberSaveable(input, saver = LazyGridState.Saver) {
        LazyGridState(firstVisibleItemIndex, firstVisibleItemScrollOffset)
    }
}

fun <T> LazyListScope.sectionScaffold(
    result: Result<List<T>, Throwable>,
    itemContent: @Composable (T?) -> Unit
) {
    when (result) {
        is Result.Loading -> { // Show placeholders
            items(10) { _ ->
                itemContent(null)
            }
        }
        is Result.Success -> {
            items(result.data) { item ->
                itemContent(item)
            }
        }
        is Result.Error -> {
            item {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

fun <T> LazyGridScope.sectionScaffold(
    result: Result<List<T>, Throwable>,
    itemContent: @Composable (T?) -> Unit
) {
    when (result) {
        is Result.Loading -> { // Show placeholders
            items(10) { _ ->
                itemContent(null)
            }
        }
        is Result.Success -> {
            items(result.data) { item ->
                itemContent(item)
            }
        }
        is Result.Error -> {
            item {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
