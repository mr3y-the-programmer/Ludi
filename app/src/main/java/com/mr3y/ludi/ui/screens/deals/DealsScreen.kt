package com.mr3y.ludi.ui.screens.deals

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.ui.components.launchChromeCustomTab
import com.mr3y.ludi.ui.presenter.DealsViewModel
import com.mr3y.ludi.ui.presenter.connectivityState
import com.mr3y.ludi.ui.presenter.model.ConnectionState
import com.mr3y.ludi.ui.presenter.model.DealStore
import com.mr3y.ludi.ui.presenter.model.DealsState
import com.mr3y.ludi.ui.presenter.model.GiveawayPlatform
import com.mr3y.ludi.ui.presenter.model.GiveawayStore
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

@Composable
fun DealsScreen(
    modifier: Modifier = Modifier,
    viewModel: DealsViewModel = hiltViewModel()
) {
    val dealsState by viewModel.dealsState.collectAsStateWithLifecycle()
    DealsScreen(
        dealsState = dealsState,
        modifier = modifier,
        onUpdateSearchQuery = viewModel::updateSearchQuery,
        onSelectingDealStore = viewModel::addToSelectedDealsStores,
        onUnselectingDealStore = viewModel::removeFromSelectedDealsStores,
        onSelectingGiveawayStore = viewModel::addToSelectedGiveawaysStores,
        onUnselectingGiveawayStore = viewModel::removeFromSelectedGiveawaysStores,
        onSelectingGiveawayPlatform = viewModel::addToSelectedGiveawaysPlatforms,
        onUnselectingGiveawayPlatform = viewModel::removeFromSelectedGiveawayPlatforms
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealsScreen(
    dealsState: DealsState,
    modifier: Modifier = Modifier,
    onUpdateSearchQuery: (String) -> Unit,
    onSelectingDealStore: (DealStore) -> Unit,
    onUnselectingDealStore: (DealStore) -> Unit,
    onSelectingGiveawayStore: (GiveawayStore) -> Unit,
    onUnselectingGiveawayStore: (GiveawayStore) -> Unit,
    onSelectingGiveawayPlatform: (GiveawayPlatform) -> Unit,
    onUnselectingGiveawayPlatform: (GiveawayPlatform) -> Unit
) {
    var selectedTab by rememberSaveable(Unit) { mutableStateOf(0) }
    val topBarScrollState = rememberKeyedTopAppBarState(input = selectedTab)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarScrollState)
    var showFiltersSheet by rememberSaveable(Unit) { mutableStateOf(false) }
    val sheetType by remember {
        derivedStateOf {
            if (selectedTab == 0) BottomSheetType.Deals else BottomSheetType.Giveaways
        }
    }
    val listState = rememberKeyedLazyListState(input = selectedTab)
    Column(
        modifier = modifier.padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        val tabsLabels = listOf(stringResource(R.string.deals_label), stringResource(R.string.giveaways_label))
        SegmentedTabRow(
            numOfTabs = tabsLabels.size,
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp)
        ) { index ->
            SegmentedTab(
                selected = index == selectedTab,
                label = tabsLabels[index],
                onClick = { selectedTab = index }
            )
        }
        Scaffold(
            topBar = {
                SearchFilterBar(
                    searchQuery = dealsState.searchQuery,
                    onSearchQueryValueChanged = onUpdateSearchQuery,
                    onFilterClicked = { showFiltersSheet = !showFiltersSheet },
                    showSearchBar = selectedTab == 0,
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val context = LocalContext.current
                val chromeTabToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
                val connectionState by connectivityState()
                val isInternetConnectionNotAvailable by remember {
                    derivedStateOf { connectionState != ConnectionState.Available }
                }
                AnimatedNoInternetBanner(visible = isInternetConnectionNotAvailable)
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (selectedTab == 0) {
                        sectionScaffold(
                            result = dealsState.deals
                        ) {
                            Deal(
                                dealWrapper = it,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .height(160.dp),
                                onClick = {
                                    if (it is ResourceWrapper.ActualResource) {
                                        val dealUrl =
                                            Uri.parse("https://www.cheapshark.com/redirect?dealID=${it.resource.dealID}")
                                        launchChromeCustomTab(
                                            context,
                                            dealUrl,
                                            chromeTabToolbarColor
                                        )
                                    }
                                }
                            )
                        }
                    } else {
                        sectionScaffold(
                            result = dealsState.giveaways
                        ) {
                            GamerPowerGameGiveaway(
                                giveawayWrapper = it,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .height(120.dp),
                                onClick = {
                                    if (it is ResourceWrapper.ActualResource) {
                                        launchChromeCustomTab(
                                            context,
                                            Uri.parse(it.resource.gamerPowerUrl),
                                            chromeTabToolbarColor
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    if (showFiltersSheet) {
        FiltersBottomSheet(
            dealsFiltersState = dealsState.dealsFiltersState,
            giveawaysFiltersState = dealsState.giveawaysFiltersState,
            type = sheetType,
            onDismissRequest = { showFiltersSheet = false },
            onCloseClicked = { showFiltersSheet = false },
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

fun <T> LazyListScope.sectionScaffold(
    result: Result<List<ResourceWrapper<T>>, Throwable>,
    itemContent: @Composable (ResourceWrapper<T>) -> Unit
) {
    when (result) {
        is Result.Success -> {
            itemsIndexed(result.data) { index, resourceWrapper ->
                key(index) {
                    itemContent(resourceWrapper)
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

@LudiPreview
@Composable
fun DealsScreenPreview() {
    val dealsState = remember {
        mutableStateOf(
            DealsState(
                searchQuery = "",
                deals = Result.Success(dealSamples),
                giveaways = Result.Success(otherGamesGiveawaysSamples),
                dealsFiltersState = DealsViewModel.InitialDealsFiltersState,
                giveawaysFiltersState = DealsViewModel.InitialGiveawaysFiltersState
            )
        )
    }
    LudiTheme {
        DealsScreen(
            dealsState = dealsState.value,
            modifier = Modifier.fillMaxSize(),
            {},
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}
