package com.mr3y.ludi.shared.ui.components

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import coil3.Bitmap

actual fun Bitmap.asImageBitmap(): ImageBitmap = this.asComposeImageBitmap()
