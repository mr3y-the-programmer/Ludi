package com.mr3y.ludi.shared.ui.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import coil3.Bitmap
import coil3.request.ImageRequest

actual fun ImageRequest.Builder.platformSpecificConfig(allowHardware: Boolean): ImageRequest.Builder = this

actual fun Bitmap.asImageBitmap(): ImageBitmap = this.asComposeImageBitmap()
