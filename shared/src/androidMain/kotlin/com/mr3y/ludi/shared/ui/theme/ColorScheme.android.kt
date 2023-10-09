package com.mr3y.ludi.shared.ui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun colorScheme(
    isDarkTheme: Boolean,
    useDynamicColors: Boolean
): ColorScheme {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && useDynamicColors -> {
            val context = LocalContext.current
            if (isDarkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        else -> {
            if (isDarkTheme) {
                DarkColorScheme
            } else {
                LightColorScheme
            }
        }
    }
}

actual fun isDynamicColorSupported() = true
