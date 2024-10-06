package com.mr3y.ludi.shared.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.focused
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.onImeAction
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import cafe.adriel.lyricist.LocalStrings
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.shared.ui.components.LudiAsyncImage
import com.mr3y.ludi.shared.ui.components.LudiErrorBox
import com.mr3y.ludi.shared.ui.components.RefreshIconButton
import com.mr3y.ludi.shared.ui.components.placeholder.PlaceholderHighlight
import com.mr3y.ludi.shared.ui.components.placeholder.defaultPlaceholder
import com.mr3y.ludi.shared.ui.components.placeholder.fade
import com.mr3y.ludi.shared.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.shared.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.shared.ui.resources.isDesktopPlatform
import com.mr3y.ludi.shared.ui.theme.rating_star

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectingFavouriteGamesPage(
    searchQueryText: String,
    onUpdatingSearchQueryText: (String) -> Unit,
    allGames: OnboardingGames,
    favouriteUserGames: List<FavouriteGame>,
    onAddingGameToFavourites: (FavouriteGame) -> Unit,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
    onRefresh: () -> Unit,
    refreshSignal: Int,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        val softwareKeyboard = LocalSoftwareKeyboardController.current
        val strings = LocalStrings.current
        val label = strings.on_boarding_games_page_title
        val secondaryText = strings.on_boarding_secondary_text
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth().clearAndSetSemantics {
                contentDescription = "$label\n$secondaryText"
            }
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = secondaryText,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQueryText,
                onValueChange = onUpdatingSearchQueryText,
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
                    errorContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
                ),
                leadingIcon = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.clearAndSetSemantics { }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Search),
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                },
                trailingIcon = if (searchQueryText.isNotEmpty()) {
                    {
                        IconButton(
                            onClick = { onUpdatingSearchQueryText("") },
                            modifier = Modifier.clearAndSetSemantics {
                                contentDescription = strings.games_page_clear_search_query_desc
                                onClick {
                                    onUpdatingSearchQueryText("")
                                    true
                                }
                            }
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = Icons.Filled.Close),
                                contentDescription = null,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                } else {
                    null
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { softwareKeyboard?.hide() }),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                    .semantics {
                        focused = true
                        contentDescription = strings.games_page_search_field_desc
                        onImeAction(imeActionType = ImeAction.Done) {
                            softwareKeyboard?.hide() ?: return@onImeAction false
                            true
                        }
                    }
            )
            if (isDesktopPlatform()) {
                Spacer(modifier = Modifier.width(16.dp))
                RefreshIconButton(onClick = onRefresh)
            }
        }
        AnimatedNoInternetBanner(modifier = Modifier.padding(vertical = 8.dp))
        Text(
            text = strings.games_page_suggestions_label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start
        )
        val games = allGames.games.collectAsLazyPagingItems()
        LaunchedEffect(refreshSignal) {
            if (refreshSignal > 0) {
                games.refresh()
            }
        }
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Adaptive(minSize = 64.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(236.dp),
            horizontalItemSpacing = 8.dp,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (games.loadState.refresh is LoadStateLoading) {
                items(10) { _ ->
                    GameTile(
                        game = null,
                        selected = false,
                        onToggleSelectingFavouriteGame = {},
                        modifier = Modifier.height(IntrinsicSize.Min)
                    )
                }
            }
            if (games.loadState.refresh is LoadStateNotLoading) {
                items(
                    count = games.itemCount,
                    key = games.itemKey { it.id },
                    contentType = games.itemContentType { it }
                ) { index ->
                    val game = games[index]
                    val isFavGame = game?.id in favouriteUserGames.map { it.id }
                    if (!isFavGame) {
                        GameTile(
                            game = game,
                            selected = false,
                            onToggleSelectingFavouriteGame = { onAddingGameToFavourites(it) },
                            modifier = Modifier.height(IntrinsicSize.Min)
                        )
                    }
                }
            }
            if (games.loadState.refresh is LoadStateError) {
                item {
                    LudiErrorBox(modifier = Modifier.fillMaxWidth())
                }
            }
            if (games.loadState.append is LoadStateLoading) {
                items(10) { _ ->
                    GameTile(
                        game = null,
                        selected = false,
                        onToggleSelectingFavouriteGame = {},
                        modifier = Modifier.height(IntrinsicSize.Min)
                    )
                }
            }
            if (games.loadState.append is LoadStateError) {
                item {
                    LudiErrorBox(modifier = Modifier.fillMaxWidth())
                }
            }
        }
        if (favouriteUserGames.isNotEmpty()) {
            Text(
                text = strings.games_page_selected_label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Adaptive(minSize = 64.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(236.dp),
                horizontalItemSpacing = 8.dp,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favouriteUserGames) { game ->
                    GameTile(
                        game = game,
                        selected = true,
                        onToggleSelectingFavouriteGame = { onRemovingGameFromFavourites(it) },
                        modifier = Modifier.height(IntrinsicSize.Min)
                    )
                }
            }
        }
    }
}

@Composable
fun GameTile(
    game: Game?,
    selected: Boolean,
    onToggleSelectingFavouriteGame: (FavouriteGame) -> Unit,
    modifier: Modifier = Modifier
) {
    GameTileScaffold(
        id = game?.id,
        title = game?.name,
        imageUrl = game?.imageUrl,
        rating = game?.rating,
        selected = selected,
        showPlaceholder = game == null,
        onToggleSelectingFavouriteGame = onToggleSelectingFavouriteGame,
        modifier = modifier
    )
}

@Composable
fun GameTile(
    game: FavouriteGame,
    selected: Boolean,
    onToggleSelectingFavouriteGame: (FavouriteGame) -> Unit,
    modifier: Modifier = Modifier
) {
    GameTileScaffold(
        id = game.id,
        title = game.title,
        imageUrl = game.imageUrl,
        rating = game.rating,
        selected = selected,
        showPlaceholder = false,
        onToggleSelectingFavouriteGame = onToggleSelectingFavouriteGame,
        modifier = modifier
    )
}

@Composable
private fun GameTileScaffold(
    id: Long?,
    title: String?,
    imageUrl: String?,
    rating: Float?,
    selected: Boolean,
    showPlaceholder: Boolean,
    onToggleSelectingFavouriteGame: (FavouriteGame) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    Card(
        modifier = modifier
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Checkbox,
                onClick = {
                    if (!showPlaceholder) {
                        onToggleSelectingFavouriteGame(
                            FavouriteGame(
                                id = id!!,
                                title = title!!,
                                imageUrl = imageUrl!!,
                                rating = rating!!
                            )
                        )
                    }
                }
            )
            .padding(4.dp)
            .clearAndSetSemantics {
                if (title != null && rating != null && rating != 0.0f) {
                    contentDescription = strings.games_page_game_content_desc(title, rating)
                }
                stateDescription = if (selected) {
                    strings.games_page_game_on_state_desc(title ?: "")
                } else {
                    strings.games_page_game_off_state_desc(title ?: "")
                }
                this.selected = selected
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LudiAsyncImage(
                url = imageUrl,
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp)
                    .defaultPlaceholder(
                        isVisible = showPlaceholder,
                        highlight = PlaceholderHighlight.fade(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title ?: "Name Placeholder",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    modifier = Modifier.defaultPlaceholder(
                        isVisible = showPlaceholder,
                        highlight = PlaceholderHighlight.fade(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Star),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.rating_star
                    )
                    val stringRating = rating?.toString() ?: "Rating Placeholder"
                    Text(
                        text = if (stringRating == "0.0") "N/A" else stringRating,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.defaultPlaceholder(
                            isVisible = showPlaceholder,
                            highlight = PlaceholderHighlight.fade(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            val unselectedColor = MaterialTheme.colorScheme.background
            val circleModifier = Modifier
                .size(20.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .clip(CircleShape)
            Box(
                modifier = circleModifier
                    .then(
                        if (selected) circleModifier else circleModifier.drawBehind { drawCircle(color = unselectedColor) }
                    ),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
