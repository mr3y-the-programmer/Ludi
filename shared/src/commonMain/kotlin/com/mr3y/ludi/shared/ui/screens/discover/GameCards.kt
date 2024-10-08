package com.mr3y.ludi.shared.ui.screens.discover

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import coil3.compose.AsyncImagePainter
import coil3.toBitmap
import com.kmpalette.rememberDominantColorState
import com.mr3y.ludi.shared.core.model.Game
import com.mr3y.ludi.shared.ui.components.LudiAsyncImage
import com.mr3y.ludi.shared.ui.components.LudiSuggestionChip
import com.mr3y.ludi.shared.ui.components.asImageBitmap
import com.mr3y.ludi.shared.ui.components.placeholder.defaultPlaceholder
import com.mr3y.ludi.shared.ui.components.rememberParallaxAlignment
import ludi.shared.generated.resources.Res
import ludi.shared.generated.resources.android
import ludi.shared.generated.resources.ios
import ludi.shared.generated.resources.linux
import ludi.shared.generated.resources.nintendo_switch
import ludi.shared.generated.resources.pc
import ludi.shared.generated.resources.playstation
import ludi.shared.generated.resources.xbox
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun TrendingGameCard(
    game: Game?,
    isHighlighted: Boolean,
    lazyListState: LazyListState,
    onOpenUrl: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    val dominantColorState = rememberDominantColorState(
        defaultColor = MaterialTheme.colorScheme.primaryContainer,
        defaultOnColor = MaterialTheme.colorScheme.onPrimaryContainer,
        cacheSize = 24,
        isSwatchValid = { swatch ->
            Color(swatch.bodyTextColor).contrastAgainst(surfaceColor) >= MinContrastRatio
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
    val animatedDominantColor by animateColorAsState(
        targetValue = dominantColorState.color,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "AnimatedDominantColor"
    )
    val animatedOnDominantColor by animateColorAsState(
        targetValue = dominantColorState.onColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "AnimatedOnDominantColor"
    )
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1f else 0.85f,
        animationSpec = tween(),
        label = "AnimatedScaleProperty"
    )
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
                        onOpenUrl("https://rawg.io/games/$it")
                    }
                }
            )
            .clearAndSetSemantics {
                val platforms = game?.platformsInfo?.joinToString { it.name }
                val firstGenreName = game?.genres?.firstOrNull()?.name
                // TODO: fix those default values on the right side of elvis operator
                contentDescription = if (game?.rating == 0.0f) {
                    strings.discover_page_game_not_rated_content_description(game.name, firstGenreName ?: "", platforms ?: "")
                } else {
                    strings.discover_page_game_rated_content_description(game?.name ?: "", firstGenreName ?: "", game?.rating ?: 0f, platforms ?: "")
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val imageUrl = game?.screenshots?.firstOrNull()?.imageUrl ?: game?.imageUrl
            LudiAsyncImage(
                url = imageUrl,
                onState = { state ->
                    when (state) {
                        is AsyncImagePainter.State.Success -> {
                            bitmap = state.result.image.toBitmap().asImageBitmap()
                        }
                        else -> {}
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .defaultPlaceholder(game == null),
                contentDescription = null,
                // Set a custom memory cache key to avoid overwriting the displayed image in the cache
                customMemoryCacheKey = "$imageUrl.palette",
                allowBitmapHardware = false,
                contentScale = ContentScale.Crop,
                alignment = rememberParallaxAlignment(lazyListState, key = game?.id)
            )
            game?.genres?.firstOrNull()?.name?.let {
                LudiSuggestionChip(
                    label = it,
                    containerColor = animatedDominantColor,
                    labelColor = animatedOnDominantColor,
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
                                0.1f to animatedDominantColor.copy(alpha = 0.35f),
                                0.3f to animatedDominantColor.copy(alpha = 0.55f),
                                0.5f to animatedDominantColor.copy(alpha = 0.75f),
                                0.7f to animatedDominantColor.copy(alpha = 0.95f),
                                0.9f to animatedDominantColor
                            )
                        )
                        .padding(16.dp)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LudiAsyncImage(
                        url = game.imageUrl,
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
                            color = animatedOnDominantColor,
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
                                tint = animatedOnDominantColor,
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = game.rating.toString().takeIf { it != "0.0" } ?: "N/A",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                color = animatedOnDominantColor
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
    lazyListState: LazyListState?,
    onOpenUrl: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    showGenre: Boolean = false
) {
    val strings = LocalStrings.current
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                enabled = game != null,
                onClick = {
                    game?.slug?.let {
                        onOpenUrl("https://rawg.io/games/$it")
                    }
                }
            )
            .clearAndSetSemantics {
                val platforms = game?.platformsInfo?.joinToString { it.name }
                val firstGenreName = game?.genres?.firstOrNull()?.name
                contentDescription = if (game?.rating == 0.0f) {
                    // TODO: fix those default values on the right side of elvis operator
                    strings.discover_page_game_not_rated_content_description(game.name, firstGenreName ?: "", platforms ?: "")
                } else {
                    strings.discover_page_game_rated_content_description(game?.name ?: "", firstGenreName ?: "", game?.rating ?: 0f, platforms ?: "")
                }
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LudiAsyncImage(
                url = game?.imageUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .defaultPlaceholder(game == null),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = if (lazyListState != null) {
                    rememberParallaxAlignment(lazyListState, key = game?.id)
                } else {
                    Alignment.Center
                }
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
                        game.platformsInfo?.map { it.name.mapPlatformNameToIconRes() }
                            ?.distinct()?.forEach { iconRes ->
                                if (iconRes != null) {
                                    Icon(
                                        painter = painterResource(iconRes),
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

private fun String.mapPlatformNameToIconRes(): DrawableResource? {
    return when {
        contains("playstation", ignoreCase = true) -> Res.drawable.playstation
        contains("pc", ignoreCase = true) -> Res.drawable.pc
        contains("xbox", ignoreCase = true) -> Res.drawable.xbox
        contains("Nintendo Switch", ignoreCase = true) -> Res.drawable.nintendo_switch
        contains("ios", ignoreCase = true) -> Res.drawable.ios
        contains("android", ignoreCase = true) -> Res.drawable.android
        contains("linux", ignoreCase = true) -> Res.drawable.linux
        else -> null
    }
}
