package com.mr3y.ludi.shared.ui.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.Bitmap
import coil3.request.ImageRequest
import coil3.request.allowHardware

actual fun ImageRequest.Builder.platformSpecificConfig(allowHardware: Boolean): ImageRequest.Builder = this.allowHardware(allowHardware)

actual fun Bitmap.asImageBitmap(): ImageBitmap = this.asImageBitmap()
