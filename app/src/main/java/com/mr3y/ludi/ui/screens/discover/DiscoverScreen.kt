package com.mr3y.ludi.ui.screens.discover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.placeholder
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.core.network.model.*
import com.mr3y.ludi.ui.components.LudiSectionHeader
import com.mr3y.ludi.ui.components.shortDescription
import com.mr3y.ludi.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.ui.presenter.model.*
import com.mr3y.ludi.ui.theme.LudiTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val discoverState by viewModel.discoverState.collectAsStateWithLifecycle()
    DiscoverScreen(discoverState = discoverState, modifier = modifier)
}

@Composable
fun DiscoverScreen(
    discoverState: DiscoverState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    shape = RoundedCornerShape(50),
                    elevation = 8.dp
                ) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = {
                            Text(text = "What are you looking for?")
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Transparent,
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
                        }
                    )
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Tune),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
        val headersModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
        item {
            LudiSectionHeader(text = "Trending", modifier = headersModifier)
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when(val trendingGames = discoverState.trendingGames) {
                    is Result.Success -> {
                        itemsIndexed(trendingGames.data) { index, game ->
                            key(index) {
                                TrendingGameCard(
                                    richInfoGameWrapper = game,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .size(width = 248.dp, height = 360.dp)
                                )
                            }
                        }
                    }
                    is Result.Error -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Unexpected Error happened. try to refresh, and see if the problem persist.",
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            LudiSectionHeader(text = "Free", modifier = headersModifier)
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when(val freeGames = discoverState.freeGames) {
                    is Result.Success -> {
                        itemsIndexed(freeGames.data) { index, game ->
                            key(index) {
                                FreeGameCard(
                                    freeGameWrapper = game,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .size(width = 248.dp, height = 320.dp)
                                )
                            }
                        }
                    }
                    is Result.Error -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Unexpected Error happened. try to refresh, and see if the problem persist.",
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }
        
        item { 
            LudiSectionHeader(text = "Top rated", modifier = headersModifier)
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when(val topRatedGames = discoverState.topRatedGames) {
                    is Result.Success -> {
                        itemsIndexed(topRatedGames.data) { index, game ->
                            key(index) {
                                TopRatedGameCard(
                                    richInfoGameWrapper = game,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .size(width = 248.dp, height = 440.dp)
                                )
                            }
                        }
                    }
                    is Result.Error -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Unexpected Error happened. try to refresh, and see if the problem persist.",
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun TrendingGameCard(
    richInfoGameWrapper: ResourceWrapper<RichInfoGame>,
    modifier: Modifier = Modifier
) {
    val richInfoGame = richInfoGameWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        elevation = 8.dp
    ) {
        Column {
            AsyncImage(
                model = richInfoGame?.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .placeholder(
                        visible = richInfoGameWrapper is ResourceWrapper.Placeholder,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = MaterialTheme.colors.surface.copy(
                                alpha = 0.15f
                            )
                        )
                    ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = richInfoGame?.name ?: "Title Placeholder",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            visible = richInfoGameWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )

                if (richInfoGame == null) {
                    Text(
                        text = "Platforms placeholder",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .placeholder(
                                visible = richInfoGameWrapper is ResourceWrapper.Placeholder,
                                highlight = PlaceholderHighlight.fade(
                                    highlightColor = MaterialTheme.colors.surface.copy(
                                        alpha = 0.15f
                                    )
                                )
                            )
                    )
                } else {
                    val (platformIconSize, platformIconPadding) = 24.dp to 8.dp
                    val platformIconPaddingInPx = with(LocalDensity.current) { platformIconPadding.toPx() }
                    val platformIconSizeWithPaddingInPx = with(LocalDensity.current) { (platformIconSize + platformIconPadding).toPx() }
                    val platformIconSizeInPx = with(LocalDensity.current) { platformIconSize.toPx() }
                    Layout(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(horizontal = platformIconPadding)
                            .fillMaxWidth(),
                        content = {
                            richInfoGame.platformsInfo?.map { it.name.mapPlatformNameToVectorDrawable() }?.distinct()?.forEach { drawableId ->
                                if (drawableId != null) {
                                    Icon(
                                        painter = painterResource(id = drawableId),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.onSurface
                                    )
                                }
                            }
                        }
                    ) {measurables, constraints ->
                        val numOfFittingItems = (constraints.maxWidth / platformIconSizeWithPaddingInPx).toInt()
                        val placeables = measurables.map { it.measure(constraints.copy(
                            minWidth = platformIconSizeInPx.roundToInt(), maxWidth = platformIconSizeInPx.roundToInt(),
                            minHeight = platformIconSizeInPx.roundToInt(), maxHeight = platformIconSizeInPx.roundToInt()
                        )) }
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            var laidItems = 0
                            var x = 0
                            var i = 0
                            while (laidItems <= numOfFittingItems && i < placeables.size) {
                                val placeable = placeables[i]
                                placeable.place(x = x, y = 0)
                                i++
                                laidItems++
                                x += placeable.width + if (i == placeables.lastIndex) platformIconPaddingInPx.roundToInt() / 2 else platformIconPaddingInPx.roundToInt()
                            }
                        }
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    richInfoGame?.genres?.forEach {
                        Chip(
                            onClick = { },
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .size(width = 80.dp, height = 24.dp),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, MaterialTheme.colors.shortDescription),
                            colors = ChipDefaults.outlinedChipColors(
                                contentColor = MaterialTheme.colors.shortDescription,
                                backgroundColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.shortDescription,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FreeGameCard(
    freeGameWrapper: ResourceWrapper<FreeGame>,
    modifier: Modifier = Modifier
) {
    val freeGame = freeGameWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        elevation = 8.dp
    ) {
        Column {
            AsyncImage(
                model = freeGame?.thumbnailUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .placeholder(
                        visible = freeGameWrapper is ResourceWrapper.Placeholder,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = MaterialTheme.colors.surface.copy(
                                alpha = 0.15f
                            )
                        )
                    ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = freeGame?.title ?: "Title Placeholder",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            visible = freeGameWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )
                Text(
                    text = freeGame?.publisher ?: "Publisher placeholder",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            visible = freeGameWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )
                freeGame?.genre?.let {
                    Chip(
                        onClick = { },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .size(width = 80.dp, height = 24.dp),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, MaterialTheme.colors.shortDescription),
                        colors = ChipDefaults.outlinedChipColors(
                            contentColor = MaterialTheme.colors.shortDescription,
                            backgroundColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.shortDescription,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun TopRatedGameCard(
    richInfoGameWrapper: ResourceWrapper<RichInfoGame>,
    modifier: Modifier
) {
    val richInfoGame = richInfoGameWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        elevation = 8.dp
    ) {
        Column {
            AsyncImage(
                model = richInfoGame?.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .placeholder(
                        visible = richInfoGameWrapper is ResourceWrapper.Placeholder,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = MaterialTheme.colors.surface.copy(
                                alpha = 0.15f
                            )
                        )
                    ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = richInfoGame?.name ?: "Title Placeholder",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            visible = richInfoGameWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )

                if (richInfoGame == null) {
                    Text(
                        text = "Platforms placeholder",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .placeholder(
                                visible = richInfoGameWrapper is ResourceWrapper.Placeholder,
                                highlight = PlaceholderHighlight.fade(
                                    highlightColor = MaterialTheme.colors.surface.copy(
                                        alpha = 0.15f
                                    )
                                )
                            )
                    )
                } else {
                    val (platformIconSize, platformIconPadding) = 24.dp to 8.dp
                    val platformIconPaddingInPx = with(LocalDensity.current) { platformIconPadding.toPx() }
                    val platformIconSizeWithPaddingInPx = with(LocalDensity.current) { (platformIconSize + platformIconPadding).toPx() }
                    val platformIconSizeInPx = with(LocalDensity.current) { platformIconSize.toPx() }
                    Layout(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(horizontal = platformIconPadding)
                            .fillMaxWidth(),
                        content = {
                            richInfoGame.platformsInfo?.map { it.name.mapPlatformNameToVectorDrawable() }?.distinct()?.forEach { drawableId ->
                                if (drawableId != null) {
                                    Icon(
                                        painter = painterResource(id = drawableId),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.onSurface
                                    )
                                }
                            }
                        }
                    ) {measurables, constraints ->
                        val numOfFittingItems = (constraints.maxWidth / platformIconSizeWithPaddingInPx).toInt()
                        val placeables = measurables.map { it.measure(constraints.copy(
                            minWidth = platformIconSizeInPx.roundToInt(), maxWidth = platformIconSizeInPx.roundToInt(),
                            minHeight = platformIconSizeInPx.roundToInt(), maxHeight = platformIconSizeInPx.roundToInt()
                        )) }
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            var laidItems = 0
                            var x = 0
                            var i = 0
                            while (laidItems <= numOfFittingItems && i < placeables.size) {
                                val placeable = placeables[i]
                                placeable.place(x = x, y = 0)
                                i++
                                laidItems++
                                x += placeable.width + if (i == placeables.lastIndex) platformIconPaddingInPx.roundToInt() / 2 else platformIconPaddingInPx.roundToInt()
                            }
                        }
                    }
                }
                richInfoGame?.rating?.let {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Star),
                            contentDescription = null,
                            tint = MaterialTheme.colors.star
                        )

                        Text(
                            text = it.toString(),
                            style = MaterialTheme.typography.subtitle1,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.shortDescription,
                        )
                    }

                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    richInfoGame?.genres?.forEach {
                        Chip(
                            onClick = { },
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .size(width = 80.dp, height = 24.dp),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, MaterialTheme.colors.shortDescription),
                            colors = ChipDefaults.outlinedChipColors(
                                contentColor = MaterialTheme.colors.shortDescription,
                                backgroundColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.shortDescription,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

val Colors.star: Color
    get() = if (isLight) Color(0xFFEAC54F) else Color(0xFFE3B341)

private fun String.mapPlatformNameToVectorDrawable(): Int? {
    return when {
        contains("playstation", ignoreCase = true) -> R.drawable.playstation
        contains("pc", ignoreCase = true) -> R.drawable.pc
        contains("xbox", ignoreCase = true) -> R.drawable.xbox
        contains("Nintendo Switch", ignoreCase = true) -> R.drawable.nintendo_switch
        contains("ios", ignoreCase = true) -> R.drawable.ios
        contains("android", ignoreCase = true) -> R.drawable.android
        contains("linux", ignoreCase = true) -> R.drawable.linux
        else -> null
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun DiscoverScreenPreview() {
    LudiTheme {
        DiscoverScreen(
            discoverState = DiscoverState(
                freeGames = Result.Success(freeGamesSamples),
                trendingGames = Result.Success(richInfoGamesSamples),
                topRatedGames = Result.Success(richInfoGamesSamples)
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}