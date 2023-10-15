package com.mr3y.ludi.shared.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.seiko.imageloader.toPainter
import kotlinx.coroutines.flow.onStart

@Composable
actual fun AsyncImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier,
    placeholder: Painter?,
    error: Painter?,
    onState: ((State) -> Unit)?,
    contentScale: ContentScale,
    alignment: Alignment,
    customMemoryCacheKey: String?,
    filterQuality: FilterQuality
) {
    val request = remember(url) {
        ImageRequest {
            data(url)
            components {
                add { _, _, _ -> customMemoryCacheKey }
            }
        }
    }
    val imageLoader = LocalImageLoader.current
    val imageAction by rememberImageAction(request)
    LaunchedEffect(request, imageLoader) {
        snapshotFlow { imageAction }
            .onStart { onState?.invoke(State.Empty) }
            .collect { action ->
                when (action) {
                    is ImageEvent -> onState?.invoke(State.Loading(placeholder))
                    is ImageResult.Bitmap -> {
                        val bitmap = action.bitmap
                        onState?.invoke(State.Success(painter = bitmap.toPainter(filterQuality), result = SuccessResult(bitmap.asComposeImageBitmap())))
                    }
                    is ImageResult.Image -> {
                        val image = action.image
                        onState?.invoke(State.Success(painter = image.toPainter(), result = SuccessResult(image.toComposeImageBitmap())))
                    }
                    is ImageResult.Painter -> {}
                    is ImageResult.Error -> onState?.invoke(State.Error(error, ErrorResult(action.error)))
                    is ImageResult.Source -> onState?.invoke(State.Error(error, ErrorResult(null)))
                }
            }
    }
    Image(
        painter = rememberImageActionPainter(
            imageAction,
            filterQuality,
            placeholderPainter = if (placeholder != null) {
                { remember { placeholder } }
            } else {
                null
            },
            errorPainter = if (error != null) {
                { remember { error } }
            } else {
                null
            }
        ),
        contentDescription = contentDescription,
        contentScale = contentScale,
        alignment = alignment,
        modifier = modifier
    )
}
