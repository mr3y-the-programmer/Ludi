package com.mr3y.ludi.ui.screens.discover

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.hours

@Composable
internal fun rememberDominantColorsState(
    defaultColor: Color = MaterialTheme.colorScheme.primaryContainer,
    defaultOnColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    surfaceColor: Color = MaterialTheme.colorScheme.surfaceVariant
): DominantColorsState {
    return remember(surfaceColor) {
        DominantColorsState(defaultColor, defaultOnColor, surfaceColor)
    }
}

@Immutable
data class DominantColors(val color: Color, val onColor: Color)

private val cache by lazy {
    Cache.Builder<Any, DominantColors>()
        .expireAfterAccess(1.hours)
        .maximumCacheSize(100)
        .build()
}

class DominantColorsState(
    val defaultColor: Color,
    val defaultOnColor: Color,
    private val surfaceColor: Color,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun extractDominantColorsFrom(context: Context, imageUrl: String): DominantColors {
        val compoundKey = compoundKey(imageUrl, surfaceColor)
        return cache.get(compoundKey) {
            calculateSwatchesInBitmap(context, imageUrl)
                // First we want to sort the list by the color's population
                .sortedByDescending { swatch -> swatch.population }
                // Then we want to find the first valid color
                .firstOrNull { swatch ->
                    Color(swatch.bodyTextColor).contrastAgainst(surfaceColor) >= MinContrastRatio
                }?.let { swatch ->
                    DominantColors(
                        color = Color(swatch.rgb),
                        onColor = Color(swatch.bodyTextColor).copy(alpha = 1f)
                    )
                } ?: DominantColors(defaultColor, defaultOnColor)
        }
    }

    private suspend fun calculateSwatchesInBitmap(context: Context, imageUrl: String): List<Palette.Swatch> {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            // We scale the image to cover 128px x 128px (i.e. min dimension == 128px)
            .size(128).scale(Scale.FILL)
            // Disable hardware bitmaps, since Palette uses Bitmap.getPixels()
            .allowHardware(false)
            // Set a custom memory cache key to avoid overwriting the displayed image in the cache
            .memoryCacheKey("$imageUrl.palette")
            .build()

        val bitmap = when (val result = context.imageLoader.execute(request)) {
            is SuccessResult -> result.drawable.toBitmap()
            else -> null
        }

        return bitmap?.let {
            withContext(coroutineDispatcher) {
                val palette = Palette.Builder(bitmap)
                    // Disable any bitmap resizing in Palette. We've already loaded an appropriately
                    // sized bitmap through Coil
                    .resizeBitmapArea(0)
                    // Clear any built-in filters. We want the unfiltered dominant color
                    .clearFilters()
                    // We reduce the maximum color count down to 8
                    .maximumColorCount(8)
                    .generate()

                palette.swatches
            }
        } ?: emptyList()
    }

    private fun compoundKey(key1: Any, key2: Any): Int {
        var result = key1.hashCode()
        result = 31 * result + key2.hashCode()
        return result
    }

    companion object {
        private const val MinContrastRatio = 3f
    }
}

fun Color.contrastAgainst(background: Color): Float {
    val fg = if (alpha < 1f) compositeOver(background) else this

    val fgLuminance = fg.luminance() + 0.05f
    val bgLuminance = background.luminance() + 0.05f

    return maxOf(fgLuminance, bgLuminance) / minOf(fgLuminance, bgLuminance)
}
