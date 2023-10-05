package com.mr3y.ludi.shared.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun colorScheme(
    isDarkTheme: Boolean,
    useDynamicColors: Boolean
): ColorScheme {
    return if (isDarkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
}
