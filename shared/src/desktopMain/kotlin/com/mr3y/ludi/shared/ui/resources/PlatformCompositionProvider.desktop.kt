package com.mr3y.ludi.shared.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.mr3y.ludi.shared.ui.components.generateImageLoader
import com.seiko.imageloader.LocalImageLoader

@Composable
actual fun ProvideCompositionLocalValues(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
        content = content
    )
}
