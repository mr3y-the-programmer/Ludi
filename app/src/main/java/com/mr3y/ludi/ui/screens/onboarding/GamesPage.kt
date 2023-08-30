package com.mr3y.ludi.ui.screens.onboarding

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
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.focused
import androidx.compose.ui.semantics.imeAction
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.performImeAction
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.components.defaultPlaceholder
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.screens.discover.gamesSamples
import com.mr3y.ludi.ui.theme.LudiTheme
import com.mr3y.ludi.ui.theme.rating_star

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectingFavouriteGamesPage(
    searchQueryText: String,
    onUpdatingSearchQueryText: (String) -> Unit,
    allGames: OnboardingGames,
    favouriteUserGames: List<FavouriteGame>,
    onAddingGameToFavourites: (FavouriteGame) -> Unit,
    onRemovingGameFromFavourites: (FavouriteGame) -> Unit,
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
        val label = stringResource(R.string.on_boarding_games_page_title)
        val secondaryText = stringResource(R.string.on_boarding_secondary_text)
        val context = LocalContext.current
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.clearAndSetSemantics {
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

        TextField(
            value = searchQueryText,
            onValueChange = onUpdatingSearchQueryText,
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
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
                            contentDescription = context.getString(R.string.games_page_clear_search_query_desc)
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
                .align(Alignment.CenterHorizontally)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(50))
                .semantics {
                    focused = true
                    contentDescription = context.getString(R.string.games_page_search_field_desc)
                    imeAction = ImeAction.Done
                    performImeAction {
                        softwareKeyboard?.hide() ?: return@performImeAction false
                        true
                    }
                }
        )
        AnimatedNoInternetBanner(modifier = Modifier.padding(vertical = 8.dp))
        Text(
            text = stringResource(R.string.games_page_suggestions_label),
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
            when (val result = allGames.games) {
                is Result.Loading -> {
                    items(10) { _ ->
                        GameTile(
                            game = null,
                            selected = false,
                            onToggleSelectingFavouriteGame = {},
                            modifier = Modifier.height(IntrinsicSize.Min)
                        )
                    }
                }
                is Result.Success -> {
                    result.data.forEach { game ->
                        val isFavGame = game.let { FavouriteGame(it.id, it.name, it.imageUrl, it.rating) } in favouriteUserGames
                        if (!isFavGame) {
                            item {
                                GameTile(
                                    game = game,
                                    selected = false,
                                    onToggleSelectingFavouriteGame = { onAddingGameToFavourites(it) },
                                    modifier = Modifier.height(IntrinsicSize.Min)
                                )
                            }
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
        if (favouriteUserGames.isNotEmpty()) {
            Text(
                text = stringResource(R.string.games_page_selected_label),
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
    val context = LocalContext.current
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
                    contentDescription =
                        context.getString(R.string.games_page_game_content_desc, title, rating)
                }
                stateDescription = if (selected) {
                    context.getString(
                        R.string.games_page_game_on_state_desc,
                        title
                    )
                } else {
                    context.getString(R.string.games_page_game_off_state_desc, title)
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
            AsyncImage(
                model = imageUrl,
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

@LudiPreview
@Composable
fun SelectingFavouriteGamesPagePreview() {
    LudiTheme {
        SelectingFavouriteGamesPage(
            searchQueryText = "",
            onUpdatingSearchQueryText = {},
            allGames = PreviewOnboardingGames,
            favouriteUserGames = emptyList(),
            onAddingGameToFavourites = {},
            onRemovingGameFromFavourites = {}
        )
    }
}

@LudiPreview
@Composable
fun GameTilePreview() {
    LudiTheme {
        GameTile(
            game = gamesSamples.first(),
            selected = false,
            onToggleSelectingFavouriteGame = {}
        )
    }
}
