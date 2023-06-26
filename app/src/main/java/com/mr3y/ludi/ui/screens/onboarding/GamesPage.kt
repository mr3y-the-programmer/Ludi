package com.mr3y.ludi.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.key
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.components.defaultPlaceholder
import com.mr3y.ludi.ui.presenter.model.FavouriteGame
import com.mr3y.ludi.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.screens.discover.richInfoGamesSamples
import com.mr3y.ludi.ui.theme.LudiTheme
import com.mr3y.ludi.ui.theme.dark_star

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,
)
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
        Text(
            text = "Tell us about your favourite games",
            modifier = Modifier.align(Alignment.End),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "You can always change that later",
            modifier = Modifier.align(Alignment.End),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )

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
                    onClick = { }
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
                        onClick = { onUpdatingSearchQueryText("") }
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
        )
        Text(
            text = "Suggestions",
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
                is Result.Success -> {
                    result.data.forEachIndexed { index, gameWrapper ->
                        val game = gameWrapper.actualResource
                        val isFavGame = game?.let { FavouriteGame(it.id, it.name, it.imageUrl, it.rating) } in favouriteUserGames
                        if (!isFavGame) {
                            item {
                                key(index) {
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
                text = "Selected",
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
    game: RichInfoGame?,
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
    Card(
        modifier = modifier
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
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
            .padding(4.dp),
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
                    .defaultPlaceholder(showPlaceholder),
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
                    modifier = Modifier.defaultPlaceholder(showPlaceholder)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Star),
                        contentDescription = null,
                        tint = dark_star
                    )
                    val stringRating = rating?.toString() ?: "Rating Placeholder"
                    Text(
                        text = if (stringRating == "0.0") "N/A" else stringRating,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.defaultPlaceholder(showPlaceholder)
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


@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun SelectingFavouriteGamesPagePreview() {
    LudiTheme {
        SelectingFavouriteGamesPage(
            searchQueryText = "",
            onUpdatingSearchQueryText = {},
            allGames = PreviewOnboardingGames,
            favouriteUserGames = emptyList(),
            onAddingGameToFavourites = {},
            onRemovingGameFromFavourites = {},
        )
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun GameTilePreview() {
    LudiTheme {
        GameTile(
            game = richInfoGamesSamples.first().actualResource,
            selected = false,
            onToggleSelectingFavouriteGame = {}
        )
    }
}