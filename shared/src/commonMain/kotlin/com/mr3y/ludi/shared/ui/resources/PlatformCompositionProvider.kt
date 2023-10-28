package com.mr3y.ludi.shared.ui.resources

import androidx.compose.runtime.Composable

/**
 * Each platform may want to provide specific CompositionLocals
 */
@Composable
expect fun ProvideCompositionLocalValues(content: @Composable () -> Unit)

expect fun isDesktopPlatform(): Boolean
