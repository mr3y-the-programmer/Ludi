package com.mr3y.ludi.shared.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
actual fun ProvideCompositionLocalValues(content: @Composable () -> Unit) = CompositionLocalProvider(content = content)
