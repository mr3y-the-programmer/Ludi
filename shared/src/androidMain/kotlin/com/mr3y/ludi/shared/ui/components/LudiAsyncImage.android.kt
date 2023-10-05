package com.mr3y.ludi.shared.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult

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
    val context = LocalContext.current
    coil.compose.AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .memoryCacheKey(customMemoryCacheKey)
            .build(),
        contentDescription = contentDescription,
        transform = { state ->
            when(state) {
                is AsyncImagePainter.State.Loading -> AsyncImagePainter.State.Loading(painter = placeholder)
                is AsyncImagePainter.State.Error -> AsyncImagePainter.State.Error(painter = error, result = state.result)
                else -> state
            }
        },
        onState = { coilState ->
            when(coilState) {
                is AsyncImagePainter.State.Empty -> onState?.invoke(State.Empty)
                is AsyncImagePainter.State.Loading -> onState?.invoke(State.Loading(coilState.painter))
                is AsyncImagePainter.State.Success -> onState?.invoke(State.Success(painter = coilState.painter, result = coilState.result.toOurSuccessResult()))
                is AsyncImagePainter.State.Error -> onState?.invoke(State.Error(painter = coilState.painter, result = ErrorResult(coilState.result.throwable)))
            }
        },
        contentScale = contentScale,
        alignment = alignment,
        filterQuality = filterQuality,
        modifier = modifier
    )
}

private fun SuccessResult.toOurSuccessResult(): com.mr3y.ludi.shared.ui.components.SuccessResult {
    return SuccessResult(this.drawable.toBitmap().asImageBitmap())
}