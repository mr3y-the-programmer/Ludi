package com.mr3y.ludi.ui.screens.deals

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.components.LudiFilterChip
import com.mr3y.ludi.ui.presenter.DealsViewModel
import com.mr3y.ludi.ui.presenter.model.DealsFiltersState
import com.mr3y.ludi.ui.presenter.model.DealsState
import com.mr3y.ludi.ui.presenter.model.DealStore
import com.mr3y.ludi.ui.presenter.model.Genre
import com.mr3y.ludi.ui.presenter.model.GiveawayPlatform
import com.mr3y.ludi.ui.presenter.model.GiveawayStore
import com.mr3y.ludi.ui.presenter.model.GiveawaysFiltersState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
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
        modifier = modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        val tabsLabels = listOf("Deals", "Giveaways")
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
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                if (selectedTab == 0) {
                    SectionScaffold(
                        result = dealsState.deals
                    ) {
                        Deal(
                            dealWrapper = it,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    }
                } else {
                    SectionScaffold(
                        result = dealsState.mmoGamesGiveaways
                    ) {
                        MMOGameGiveaway(
                            giveawayWrapper = it,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    }
                    SectionScaffold(
                        result = dealsState.otherGamesGiveaways
                    ) {
                        GamerPowerGameGiveaway(
                            giveawayWrapper = it,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(120.dp)
                        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterBar(
    searchQuery: String,
    onSearchQueryValueChanged: (String) -> Unit,
    onFilterClicked: () -> Unit,
    showSearchBar: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            if (showSearchBar) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryValueChanged,
                    shape = RoundedCornerShape(50),
                    placeholder = {
                        Text(text = "Search for a specific deal")
                    },
                    colors = TextFieldDefaults.colors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = Icons.Filled.Search),
                                contentDescription = null,
                                modifier = Modifier.fillMaxHeight(),
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth()
                )
            }
        },
        actions = {
            IconButton(
                onClick = onFilterClicked,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .size(48.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Tune),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets(0.dp)
    )
}

fun <T> LazyListScope.SectionScaffold(
    result: Result<List<ResourceWrapper<T>>, Throwable>,
    itemContent: @Composable (ResourceWrapper<T>) -> Unit
) {
    when(result) {
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

internal enum class BottomSheetType {
    Deals,
    Giveaways
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun FiltersBottomSheet(
    dealsFiltersState: DealsFiltersState,
    giveawaysFiltersState: GiveawaysFiltersState,
    type: BottomSheetType,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCloseClicked: () -> Unit,
    onSelectingGiveawayPlatform: (GiveawayPlatform) -> Unit,
    onUnselectingGiveawayPlatform: (GiveawayPlatform) -> Unit,
    onSelectingDealStore: (DealStore) -> Unit,
    onUnselectingDealStore: (DealStore) -> Unit,
    onSelectingGiveawayStore: (GiveawayStore) -> Unit,
    onUnselectingGiveawayStore: (GiveawayStore) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = null,
        modifier = modifier
    ) {
        val chipModifier = Modifier
            .padding(vertical = 4.dp)
            .width(IntrinsicSize.Max)
            .animateContentSize()
        OutlinedButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.tertiary
            ),
            border = BorderStroke(0.dp, Color.Transparent),
            onClick = onCloseClicked
        ) {
            Text(
                text = "Close",
            )
        }
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            if (type == BottomSheetType.Deals) {
                Text(
                    text = "Stores:",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dealsFiltersState.allStores.forEach {
                        LudiFilterChip(
                            selected = it in dealsFiltersState.selectedStores,
                            label = it.label,
                            modifier = chipModifier,
                            onClick = {
                                if (it in dealsFiltersState.selectedStores)
                                    onUnselectingDealStore(it)
                                else
                                    onSelectingDealStore(it)
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = "Stores: ",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    giveawaysFiltersState.allStores.forEach {
                        LudiFilterChip(
                            selected = it in giveawaysFiltersState.selectedStores,
                            label = it.name,
                            modifier = chipModifier,
                            onClick = {
                                if (it in giveawaysFiltersState.selectedStores)
                                    onUnselectingGiveawayStore(it)
                                else
                                    onSelectingGiveawayStore(it)
                            }
                        )
                    }
                }
                Text(
                    text = "Platforms: ",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    giveawaysFiltersState.allPlatforms.forEach {
                        LudiFilterChip(
                            selected = it in giveawaysFiltersState.selectedPlatforms,
                            label = it.name,
                            modifier = chipModifier,
                            onClick = {
                                if (it in giveawaysFiltersState.selectedPlatforms)
                                    onUnselectingGiveawayPlatform(it)
                                else
                                    onSelectingGiveawayPlatform(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = "id:pixel_6")
@Composable
fun DealsScreenPreview() {
    val dealsState = remember {
        mutableStateOf(
            DealsState(
                searchQuery = "",
                deals = Result.Success(dealSamples),
                mmoGamesGiveaways = Result.Success(mmoGiveawaysSamples),
                otherGamesGiveaways = Result.Success(otherGamesGiveawaysSamples),
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
            {},
        )
    }
}