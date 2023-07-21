package com.mr3y.ludi.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.components.LudiSectionHeader
import com.mr3y.ludi.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.ui.presenter.connectivityState
import com.mr3y.ludi.ui.presenter.groupByGenre
import com.mr3y.ludi.ui.presenter.model.ConnectionState
import com.mr3y.ludi.ui.presenter.model.DiscoverState
import com.mr3y.ludi.ui.presenter.model.DiscoverStateGames
import com.mr3y.ludi.ui.presenter.model.Genre
import com.mr3y.ludi.ui.presenter.model.Platform
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.Store
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.preview.LudiPreview
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
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val connectionState by connectivityState()
            val isInternetConnectionNotAvailable by remember {
                derivedStateOf { connectionState != ConnectionState.Available }
            }
            when (discoverState.games) {
                is DiscoverStateGames.SuggestedGames -> {
                    AnimatedNoInternetBanner(visible = isInternetConnectionNotAvailable)
                    SuggestedGamesPage(
                        suggestedGames = discoverState.games
                    )
                }
                else -> {
                    discoverState.games as DiscoverStateGames.SearchQueryBasedGames
                    AnimatedNoInternetBanner(visible = isInternetConnectionNotAvailable)
                    SearchQueryAndFilterPage(
                        searchResult = discoverState.games.games
                    )
                }
            }
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
                modifier = sectionsModifier,
                showGenre = true
            )
        }

        item {
            LudiSectionHeader(text = "Top rated", modifier = headersModifier)
        }
        item {
            RichInfoGamesSection(
                games = suggestedGames.topRatedGames,
                modifier = sectionsModifier,
                showGenre = true
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
    searchResult: Result<ResourceWrapper<Map<GameGenre, List<Game>>>, Throwable>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (searchResult) {
            is Result.Success -> {
                if (searchResult.data is ResourceWrapper.ActualResource) {
                    searchResult.data.resource.forEach { (gameGenre, games) ->
                        item {
                            LudiSectionHeader(
                                text = gameGenre.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )
                        }
                        item {
                            LazyRow(
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
                } else {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
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
    games: Result<List<ResourceWrapper<Game>>, Throwable>,
    modifier: Modifier = Modifier,
    showGenre: Boolean = false
) {
    GamesSectionScaffold(
        gamesResult = games,
        modifier = modifier
    ) { gameWrapper ->
        GameCard(
            game = gameWrapper.actualResource,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .width(200.dp)
                .height(280.dp),
            showGenre = showGenre
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
                items(gamesResult.data) { gameWrapper ->
                    itemContent(gameWrapper)
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
        DiscoverScreen(
            discoverState = DiscoverState(
                searchQuery = "",
                games = DiscoverStateGames.SuggestedGames(
                    trendingGames = Result.Success(gamesSamples),
                    topRatedGames = Result.Success(gamesSamples),
                    multiplayerGames = Result.Success(gamesSamples),
                    familyGames = Result.Success(gamesSamples),
                    realisticGames = Result.Success(gamesSamples)
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

@LudiPreview
@Composable
fun SearchResultsPagePreview() {
    LudiTheme {
        SearchQueryAndFilterPage(
            searchResult = Result.Success(ResourceWrapper.ActualResource(gamesSamples.map { it.resource }.groupByGenre())),
            modifier = Modifier.fillMaxSize()
        )
    }
}
