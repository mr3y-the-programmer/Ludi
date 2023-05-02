package com.mr3y.ludi.ui.screens.discover

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.components.LudiFilterChip
import com.mr3y.ludi.ui.components.LudiSectionHeader
import com.mr3y.ludi.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.ui.presenter.model.DiscoverState
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.Genre
import com.mr3y.ludi.ui.presenter.model.Platform
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.Store
import com.mr3y.ludi.ui.theme.LudiTheme

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val discoverState by viewModel.discoverState.collectAsStateWithLifecycle()
    DiscoverScreen(
        discoverState = discoverState,
        onUpdatingSearchQueryText = viewModel::updateSearchQuery,
        onSelectingPlatform = viewModel::addToSelectedPlatforms,
        onUnselectingPlatform = viewModel::removeFromSelectedPlatforms,
        onSelectingStore = viewModel::addToSelectedStores,
        onUnselectingStore = viewModel::removeFromSelectedStores,
        onSelectingGenre = viewModel::addToSelectedGenres,
        onUnselectingGenre = viewModel::removeFromSelectedGenres,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    discoverState: DiscoverState,
    onUpdatingSearchQueryText: (String) -> Unit,
    onSelectingPlatform: (Platform) -> Unit,
    onUnselectingPlatform: (Platform) -> Unit,
    onSelectingStore: (Store) -> Unit,
    onUnselectingStore: (Store) -> Unit,
    onSelectingGenre: (Genre) -> Unit,
    onUnselectingGenre: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    var openFiltersSheet by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
        }
    ) { contentPadding ->
        if (discoverState.games is DiscoverStateGames.SuggestedGames) {
            SuggestedGamesPage(
                suggestedGames = discoverState.games,
                modifier = Modifier.padding(contentPadding)
            )
        } else {
            discoverState.games as DiscoverStateGames.SearchQueryBasedGames
            SearchQueryAndFilterPage(
                searchResult = discoverState.games.richInfoGames,
                modifier = Modifier.padding(contentPadding)
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
            onSelectingGenre,
            onUnselectingGenre
        )
    }
}

@Composable
fun SuggestedGamesPage(
    suggestedGames: DiscoverStateGames.SuggestedGames,
    modifier: Modifier = Modifier
) {
    val headersModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)

    val sectionsModifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(horizontal = 8.dp)
    LazyColumn(
        modifier = modifier
    ) {
        item {
            LudiSectionHeader(text = "Trending", modifier = headersModifier)
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.trendingGames,
                modifier = sectionsModifier
            )
        }

        item {
            LudiSectionHeader(text = "Free", modifier = headersModifier)
        }
        item {
            FreeGamesSection(
                freeGames = suggestedGames.freeGames,
                modifier = sectionsModifier
            )
        }

        item {
            LudiSectionHeader(text = "Top rated", modifier = headersModifier)
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.topRatedGames,
                modifier = sectionsModifier
            )
        }

        item {
            LudiSectionHeader(text = "Multiplayer", modifier = headersModifier)
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.multiplayerGames,
                modifier = sectionsModifier
            )
        }

        item {
            LudiSectionHeader(text = "Family", modifier = headersModifier)
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.familyGames,
                modifier = sectionsModifier
            )
        }

        item {
            LudiSectionHeader(text = "Realistic", modifier = headersModifier)
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.realisticGames,
                modifier = sectionsModifier
            )
        }
    }
}

@Composable
fun SearchQueryAndFilterPage(
    searchResult: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (searchResult) {
            is Result.Success -> {
                itemsIndexed(searchResult.data) { index, gameWrapper ->
                    key(index) {
                        SearchResultsGameCard(
                            richInfoGameWrapper = gameWrapper,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverTopBar(
    searchQuery: String,
    onSearchQueryValueChanged: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryValueChanged,
                placeholder = {
                    Text(text = "What are you looking for?")
                },
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
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth()
            )
        },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = onCloseClicked,
                modifier = Modifier.requiredSize(48.dp)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Tune),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun RichInfoGamesSection(
    games: Result<List<ResourceWrapper<RichInfoGame>>, Throwable>,
    modifier: Modifier = Modifier
) {
    GamesSectionScaffold(
        gamesResult = games,
        modifier = modifier
    ) { gameWrapper ->
        RichInfoGameCard(
            richInfoGameWrapper = gameWrapper,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .width(248.dp)
                .height(IntrinsicSize.Min)
        )
    }
}

@Composable
fun FreeGamesSection(
    freeGames: Result<List<ResourceWrapper<FreeGame>>, Throwable>,
    modifier: Modifier = Modifier
) {
    GamesSectionScaffold(
        gamesResult = freeGames,
        modifier = modifier
    ) { gameWrapper ->
        FreeGameCard(
            freeGameWrapper = gameWrapper,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .width(248.dp)
                .height(IntrinsicSize.Min)
        )
    }
}

@Composable
fun <T> GamesSectionScaffold(
    gamesResult: Result<List<ResourceWrapper<T>>, Throwable>,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    itemContent: @Composable (ResourceWrapper<T>) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        when (gamesResult) {
            is Result.Success -> {
                itemsIndexed(gamesResult.data) { index, gameWrapper ->
                    key(index) {
                        itemContent(gameWrapper)
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FiltersBottomSheet(
    filtersState: DiscoverFiltersState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCloseClicked: () -> Unit,
    onSelectingPlatform: (Platform) -> Unit,
    onUnselectingPlatform: (Platform) -> Unit,
    onSelectingStore: (Store) -> Unit,
    onUnselectingStore: (Store) -> Unit,
    onSelectingGenre: (Genre) -> Unit,
    onUnselectingGenre: (Genre) -> Unit
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
                text = "Close"
            )
        }
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Platforms:",
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
                filtersState.allPlatforms.forEach {
                    LudiFilterChip(
                        selected = it in filtersState.selectedPlatforms,
                        label = it.label,
                        modifier = chipModifier,
                        onClick = {
                            if (it in filtersState.selectedPlatforms) {
                                onUnselectingPlatform(it)
                            } else {
                                onSelectingPlatform(it)
                            }
                        }
                    )
                }
            }
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
                filtersState.allStores.forEach {
                    LudiFilterChip(
                        selected = it in filtersState.selectedStores,
                        label = it.label,
                        modifier = chipModifier,
                        onClick = {
                            if (it in filtersState.selectedStores) {
                                onUnselectingStore(it)
                            } else {
                                onSelectingStore(it)
                            }
                        }
                    )
                }
            }
            Text(
                text = "Genres:",
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
                filtersState.allGenres.forEach {
                    LudiFilterChip(
                        selected = it in filtersState.selectedGenres,
                        label = it.label,
                        modifier = chipModifier,
                        onClick = {
                            if (it in filtersState.selectedGenres) {
                                onUnselectingGenre(it)
                            } else {
                                onSelectingGenre(it)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun DiscoverScreenPreview() {
    LudiTheme {
        DiscoverScreen(
            discoverState = DiscoverState(
                searchQuery = "",
                games = DiscoverStateGames.SuggestedGames(
                    freeGames = Result.Success(freeGamesSamples),
                    trendingGames = Result.Success(richInfoGamesSamples),
                    topRatedGames = Result.Success(richInfoGamesSamples),
                    multiplayerGames = Result.Success(richInfoGamesSamples),
                    familyGames = Result.Success(richInfoGamesSamples),
                    realisticGames = Result.Success(richInfoGamesSamples)
                ),
                filtersState = DiscoverViewModel.InitialFiltersState
            ),
            onUpdatingSearchQueryText = {},
            onSelectingPlatform = {},
            onUnselectingPlatform = {},
            onSelectingStore = {},
            onUnselectingStore = {},
            onSelectingGenre = {},
            onUnselectingGenre = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun SearchResultsPagePreview() {
    LudiTheme {
        SearchQueryAndFilterPage(
            searchResult = Result.Success(richInfoGamesSamples),
            modifier = Modifier.fillMaxSize()
        )
    }
}
