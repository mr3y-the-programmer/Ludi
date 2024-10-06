package com.mr3y.ludi.shared.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.Bitmap
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest

@Composable
fun LudiAsyncImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = null,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    customMemoryCacheKey: String? = null,
    allowBitmapHardware: Boolean = true,
    filterQuality: FilterQuality = FilterQuality.Low
) {
    val context = LocalPlatformContext.current
    coil3.compose.AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .memoryCacheKey(customMemoryCacheKey)
            .platformSpecificConfig(allowBitmapHardware)
            .build(),
        contentDescription = contentDescription,
        transform = { state ->
            when (state) {
                is AsyncImagePainter.State.Loading -> AsyncImagePainter.State.Loading(painter = placeholder)
                is AsyncImagePainter.State.Error -> AsyncImagePainter.State.Error(painter = error, result = state.result)
                else -> state
            }
        },
        onState = onState,
        contentScale = contentScale,
        alignment = alignment,
        filterQuality = filterQuality,
        modifier = modifier
    )
}

expect fun ImageRequest.Builder.platformSpecificConfig(allowHardware: Boolean): ImageRequest.Builder

expect fun Bitmap.asImageBitmap(): ImageBitmap
