package com.mr3y.ludi.shared.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun colorScheme(
    isDarkTheme: Boolean,
    useDynamicColors: Boolean
): ColorScheme

expect fun isDynamicColorSupported(): Boolean
