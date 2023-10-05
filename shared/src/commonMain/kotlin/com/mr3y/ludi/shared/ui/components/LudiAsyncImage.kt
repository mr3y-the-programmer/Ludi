package com.mr3y.ludi.shared.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
expect fun AsyncImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = null,
    onState: ((State) -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    customMemoryCacheKey: String? = null,
    filterQuality: FilterQuality = FilterQuality.Low
)

sealed interface State {

    val painter: Painter?

    data object Empty : State {
        override val painter: Painter? get() = null
    }

    data class Loading(override val painter: Painter?) : State

    data class Success(
        override val painter: Painter,
        val result: SuccessResult,
    ) : State

    data class Error(
        override val painter: Painter?,
        val result: ErrorResult,
    ) : State
}

sealed interface ImageResult

data class SuccessResult(val bitmap: ImageBitmap) : ImageResult

data class ErrorResult(val error: Throwable?) : ImageResult
