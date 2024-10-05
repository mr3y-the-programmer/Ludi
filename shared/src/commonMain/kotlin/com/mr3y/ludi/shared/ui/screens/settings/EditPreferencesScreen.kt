package com.mr3y.ludi.shared.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.di.getScreenModel
import com.mr3y.ludi.shared.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.shared.ui.components.AsyncImage
import com.mr3y.ludi.shared.ui.components.LudiErrorBox
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.EditPreferencesViewModel
import com.mr3y.ludi.shared.ui.presenter.model.EditPreferencesState
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import org.jetbrains.compose.resources.painterResource

data class EditPreferencesScreen(val type: PreferencesType) : Screen {
    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<EditPreferencesViewModel, PreferencesType>(arg1 = type)
        val navigator = LocalNavigator.currentOrThrow

        LifecycleEffect(
            onDisposed = { navigator.dispose(this) }
        )
        EditPreferencesScreen(
            onDoneClick = { navigator.pop() },
            viewModel = screenModel
        )
    }
}

@Composable
fun EditPreferencesScreen(
    onDoneClick: () -> Unit,
    viewModel: EditPreferencesViewModel,
    modifier: Modifier = Modifier
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
    val strings = LocalStrings.current
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
                        modifier = Modifier
                            .requiredSize(48.dp)
                            .semantics(mergeDescendants = true) {
                                contentDescription = strings.edit_preferences_page_confirm_button_content_description
                            }
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
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0.dp)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp)
                .semantics {
                    isTraversalGroup = true
                },
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
                    AnimatedNoInternetBanner()
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
    val strings = LocalStrings.current
    state.allNewsDataSources.forEachIndexed { index, source ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .selectable(
                    source in state.followedNewsDataSources,
                    onClick = {
                        if (source in state.followedNewsDataSources) {
                            onUnfollowingNewsDataSource(source)
                        } else {
                            onFollowingNewsDataSource(source)
                        }
                    }
                )
                .padding(8.dp)
                .clearAndSetSemantics {
                    stateDescription =
                        if (source in state.followedNewsDataSources) {
                            strings.data_sources_page_data_source_on_state_desc(source.name)
                        } else {
                            strings.data_sources_page_data_source_off_state_desc(source.name)
                        }
                    selected = source in state.followedNewsDataSources
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(source.icon),
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
            HorizontalDivider()
        }
    }
}

@Composable
fun GamesList(
    state: EditPreferencesState.FavouriteGames,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    state.favouriteGames.forEachIndexed { index, favouriteGame ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .selectable(
                    true,
                    onClick = { onRemovingGameFromFavourites(favouriteGame) }
                )
                .padding(8.dp)
                .clearAndSetSemantics {
                    contentDescription = strings.edit_preferences_page_game_content_description(favouriteGame.title)
                    selected = true
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    url = favouriteGame.imageUrl,
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
            HorizontalDivider()
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
    when (state.allGenres) {
        is Result.Loading -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Result.Success -> {
            val genres = state.allGenres.data
            val strings = LocalStrings.current
            genres.forEachIndexed { index, gameGenre ->
                val isSelected = gameGenre.id in state.favouriteGenres.map { it.id }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .selectable(
                            isSelected,
                            onClick = {
                                if (isSelected) {
                                    onRemovingGenreFromFavourites(gameGenre)
                                } else {
                                    onAddingGenreToFavourites(gameGenre)
                                }
                            }
                        )
                        .padding(8.dp)
                        .clearAndSetSemantics {
                            stateDescription =
                                if (isSelected) {
                                    strings.edit_preferences_page_genre_on_state_desc(gameGenre.name)
                                } else {
                                    strings.edit_preferences_page_genre_off_state_desc(gameGenre.name)
                                }
                            selected = isSelected
                        },
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
                        checked = isSelected,
                        onCheckedChange = null
                    )
                }
                if (index != genres.size - 1) {
                    HorizontalDivider()
                }
            }
        }
        is Result.Error -> {
            LudiErrorBox(modifier = modifier.fillMaxWidth())
        }
    }
}
