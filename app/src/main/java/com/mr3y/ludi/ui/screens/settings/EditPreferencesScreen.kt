package com.mr3y.ludi.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.navigation.PreferencesType
import com.mr3y.ludi.ui.presenter.EditPreferencesViewModel
import com.mr3y.ludi.ui.presenter.model.EditPreferencesState
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

@Suppress("UNUSED_PARAMETER")
@Composable
fun EditPreferencesScreen(
    type: PreferencesType,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditPreferencesViewModel = hiltViewModel()
) {
    val editPreferencesState by viewModel.editPreferencesState.collectAsStateWithLifecycle()
    EditPreferencesScreen(
        editPreferencesState,
        onDoneClick = onDoneClick,
        onFollowingNewsDataSource = viewModel::followNewsDataSource,
        onUnfollowingNewsDataSource = viewModel::unFollowNewsDataSource,
        onRemovingGameFromFavourites = viewModel::removeGameFromFavourites,
        onAddingGenreToFavourites = viewModel::addToSelectedGenres,
        onRemovingGenreFromFavourites = viewModel::removeFromSelectedGenres,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPreferencesScreen(
    state: EditPreferencesState,
    onDoneClick: () -> Unit,
    onFollowingNewsDataSource: (NewsDataSource) -> Unit,
    onUnfollowingNewsDataSource: (NewsDataSource) -> Unit,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
    onAddingGenreToFavourites: (GameGenre) -> Unit,
    onRemovingGenreFromFavourites: (GameGenre) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(
                        onClick = onDoneClick,
                        modifier = Modifier.requiredSize(48.dp)
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Check),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (state) {
                is EditPreferencesState.FollowedNewsDataSources -> {
                    NewsSourcesList(
                        state = state,
                        onFollowingNewsDataSource = onFollowingNewsDataSource,
                        onUnfollowingNewsDataSource = onUnfollowingNewsDataSource
                    )
                }
                is EditPreferencesState.FavouriteGames -> {
                    GamesList(
                        state = state,
                        onRemovingGameFromFavourites = onRemovingGameFromFavourites
                    )
                }
                is EditPreferencesState.FavouriteGenres -> {
                    GenresList(
                        state = state,
                        onAddingGenreToFavourites = onAddingGenreToFavourites,
                        onRemovingGenreFromFavourites = onRemovingGenreFromFavourites
                    )
                }
                is EditPreferencesState.Undefined -> { /*no-op*/ }
            }
        }
    }
}

@Composable
fun NewsSourcesList(
    state: EditPreferencesState.FollowedNewsDataSources,
    onFollowingNewsDataSource: (NewsDataSource) -> Unit,
    onUnfollowingNewsDataSource: (NewsDataSource) -> Unit,
    modifier: Modifier = Modifier
) {
    state.allNewsDataSources.forEachIndexed { index, source ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .selectable(
                    source in state.followedNewsDataSources,
                    role = Role.Button,
                    onClick = {
                        if (source in state.followedNewsDataSources) {
                            onUnfollowingNewsDataSource(source)
                        } else {
                            onFollowingNewsDataSource(source)
                        }
                    }
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(id = source.drawableId),
                    contentDescription = null,
                    tint = Color.Unspecified // instruct Icon to not override android:fillColor specified in the vector drawable
                )
                Text(
                    text = source.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Checkbox(
                checked = source in state.followedNewsDataSources,
                onCheckedChange = null
            )
        }
        if (index != state.allNewsDataSources.lastIndex) {
            Divider()
        }
    }
}

@Composable
fun GamesList(
    state: EditPreferencesState.FavouriteGames,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
    modifier: Modifier = Modifier
) {
    state.favouriteGames.forEachIndexed { index, favouriteGame ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .selectable(
                    true,
                    role = Role.Button,
                    onClick = { onRemovingGameFromFavourites(favouriteGame) }
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = favouriteGame.imageUrl,
                    modifier = Modifier.size(64.dp),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = favouriteGame.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Checkbox(
                checked = true,
                onCheckedChange = null
            )
        }
        if (index != state.favouriteGames.lastIndex) {
            Divider()
        }
    }
}

@Composable
fun GenresList(
    state: EditPreferencesState.FavouriteGenres,
    onAddingGenreToFavourites: (GameGenre) -> Unit,
    onRemovingGenreFromFavourites: (GameGenre) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.allGenres is Result.Success) {
        when (state.allGenres.data) {
            is ResourceWrapper.Placeholder -> {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ResourceWrapper.ActualResource -> {
                val genres = state.allGenres.data.resource
                genres.forEachIndexed { index, gameGenre ->
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .selectable(
                                gameGenre in state.favouriteGenres,
                                role = Role.Button,
                                onClick = {
                                    if (gameGenre in state.favouriteGenres) {
                                        onRemovingGenreFromFavourites(gameGenre)
                                    } else {
                                        onAddingGenreToFavourites(gameGenre)
                                    }
                                }
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = gameGenre.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Checkbox(
                            checked = gameGenre in state.favouriteGenres,
                            onCheckedChange = null
                        )
                    }
                    if (index != genres.size - 1) {
                        Divider()
                    }
                }
            }
        }
    } else {
        LudiErrorBox(modifier = modifier.fillMaxWidth())
    }
}

@LudiPreview
@Composable
fun EditPreferencesScreenPreview() {
    val allGenres = Result.Success(
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
                )
            )
        )
    )
    val selectedGenres = setOf(
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
        )
    )
    val state by remember {
        mutableStateOf(
            EditPreferencesState.FavouriteGenres(
                allGenres = allGenres,
                favouriteGenres = selectedGenres
            )
        )
    }
    LudiTheme {
        EditPreferencesScreen(
            state = state,
            onDoneClick = { /*TODO*/ },
            onFollowingNewsDataSource = {},
            onUnfollowingNewsDataSource = {},
            onRemovingGameFromFavourites = {},
            onAddingGenreToFavourites = {},
            onRemovingGenreFromFavourites = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
