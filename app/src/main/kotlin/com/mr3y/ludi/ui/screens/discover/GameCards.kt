package com.mr3y.ludi.ui.screens.discover

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.kmpalette.rememberDominantColorState
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.ui.components.LudiSuggestionChip
import com.mr3y.ludi.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.ui.components.defaultPlaceholder
import com.mr3y.ludi.ui.components.launchChromeCustomTab
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

@Composable
fun TrendingGameCard(
    game: Game?,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    val dominantColorState = rememberDominantColorState(
        defaultColor = MaterialTheme.colorScheme.primaryContainer,
        defaultOnColor = MaterialTheme.colorScheme.onPrimaryContainer,
        cacheSize = 24,
        isColorValid = { dominantColor ->
            dominantColor.contrastAgainst(surfaceColor) >= MinContrastRatio
        },
        builder = {
            // Clear any built-in filters. We want the unfiltered dominant color
            clearFilters()
            // We reduce the maximum color count down to 8
            .maximumColorCount(8)
        }
    )
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(bitmap) {
        val temp = bitmap
        if (temp != null) {
            dominantColorState.updateFrom(temp)
        }
    }
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1f else 0.85f,
        animationSpec = tween(),
        label = "AnimatedScaleProperty"
    )
    val context = LocalContext.current
    val chromeToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
    Card(
        shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
        modifier = modifier
            .aspectRatio(1.1f, matchHeightConstraintsFirst = true)
            .scale(scale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                enabled = game != null,
                onClick = {
                    game?.slug?.let {
                        launchChromeCustomTab(
                            context,
                            Uri.parse("https://rawg.io/games/$it"),
                            chromeToolbarColor
                        )
                    }
                }
            )
            .clearAndSetSemantics {
                val platforms = game?.platformsInfo?.joinToString { it.name }
                val firstGenreName = game?.genres?.firstOrNull()?.name
                contentDescription = if (game?.rating == 0.0f) {
                    context.getString(
                        R.string.discover_page_game_not_rated_content_description,
                        game.name,
                        firstGenreName,
                        platforms
                    )
                } else {
                    context.getString(
                        R.string.discover_page_game_rated_content_description,
                        game?.name,
                        firstGenreName,
                        game?.rating,
                        platforms
                    )
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val imageUrl = game?.screenshots?.firstOrNull()?.imageUrl ?: game?.imageUrl
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    // Set a custom memory cache key to avoid overwriting the displayed image in the cache
                    .memoryCacheKey("$imageUrl.palette")
                    .build(),
                onState = { state ->
                    when(state) {
                        is AsyncImagePainter.State.Success -> {
                            bitmap = state.result.drawable.toBitmap().asImageBitmap()
                        }
                        else -> {}
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .defaultPlaceholder(game == null),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            game?.genres?.firstOrNull()?.name?.let {
                LudiSuggestionChip(
                    label = it,
                    containerColor = dominantColorState.color,
                    labelColor = dominantColorState.onColor,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                )
            }
            if (game != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                0.1f to dominantColorState.color.copy(alpha = 0.35f),
                                0.3f to dominantColorState.color.copy(alpha = 0.55f),
                                0.5f to dominantColorState.color.copy(alpha = 0.75f),
                                0.7f to dominantColorState.color.copy(alpha = 0.95f),
                                0.9f to dominantColorState.color
                            )
                        )
                        .padding(16.dp)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = game.imageUrl,
                        modifier = Modifier
                            .width(48.dp)
                            .aspectRatio(0.75f),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = dominantColorState.onColor,
                            textAlign = TextAlign.Start,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = Icons.Filled.Star),
                                contentDescription = null,
                                tint = dominantColorState.onColor,
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = game.rating.toString().takeIf { it != "0.0" } ?: "N/A",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                color = dominantColorState.onColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameCard(
    game: Game?,
    modifier: Modifier = Modifier,
    showGenre: Boolean = false
) {
    val context = LocalContext.current
    val chromeToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                enabled = game != null,
                onClick = {
                    game?.slug?.let {
                        launchChromeCustomTab(
                            context,
                            Uri.parse("https://rawg.io/games/$it"),
                            chromeToolbarColor
                        )
                    }
                }
            )
            .clearAndSetSemantics {
                val platforms = game?.platformsInfo?.joinToString { it.name }
                val firstGenreName = game?.genres?.firstOrNull()?.name
                contentDescription = if (game?.rating == 0.0f) {
                    context.getString(
                        R.string.discover_page_game_not_rated_content_description,
                        game.name,
                        firstGenreName,
                        platforms
                    )
                } else {
                    context.getString(
                        R.string.discover_page_game_rated_content_description,
                        game?.name,
                        firstGenreName,
                        game?.rating,
                        platforms
                    )
                }
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = game?.imageUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .defaultPlaceholder(game == null),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            if (showGenre) {
                game?.genres?.firstOrNull()?.name?.let {
                    LudiSuggestionChip(
                        label = it,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
            if (game != null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                0.1f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                0.3f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                                0.5f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                                0.7f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f),
                                0.9f to MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        .padding(16.dp)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = game.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primaryContainer),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Star),
                            contentDescription = null,
                            tint = contentColorFor(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = game.rating.toString().takeIf { it != "0.0" } ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            alignment = Alignment.CenterVertically
                        )
                    ) {
                        game.platformsInfo?.map { it.name.mapPlatformNameToVectorDrawable() }
                            ?.distinct()?.forEach { drawableId ->
                                if (drawableId != null) {
                                    Icon(
                                        painter = painterResource(id = drawableId),
                                        contentDescription = null,
                                        tint = contentColorFor(MaterialTheme.colorScheme.primaryContainer),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                    }
                }
            }
        }
    }
}

private fun Color.contrastAgainst(background: Color): Float {
    val fg = if (alpha < 1f) compositeOver(background) else this

    val fgLuminance = fg.luminance() + 0.05f
    val bgLuminance = background.luminance() + 0.05f

    return maxOf(fgLuminance, bgLuminance) / minOf(fgLuminance, bgLuminance)
}

private const val MinContrastRatio = 3f

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

@LudiPreview
@Composable
fun TrendingGameCardPreview() {
    LudiTheme {
        TrendingGameCard(
            game = gamesSamples[1],
            isHighlighted = true,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@LudiPreview
@Composable
fun RichInfoGameCardPreview() {
    LudiTheme {
        GameCard(
            game = gamesSamples[2],
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Min)
                .aspectRatio(0.6f),
            showGenre = true
        )
    }
}

@LudiPreview
@Composable
fun RichInfoGameCardPlaceholderPreview() {
    LudiTheme {
        GameCard(
            game = null,
            modifier = Modifier
                .padding(16.dp)
                .size(width = 248.dp, height = 360.dp),
            showGenre = true
        )
    }
}
