package com.mr3y.ludi.shared.ui.components

import android.content.Context
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.toArgb

val ColorScheme.chromeCustomTabToolbarColor: Int
    @Composable
    @ReadOnlyComposable
    get() = primaryContainer.toArgb()

fun launchChromeCustomTab(context: Context, url: Uri, @ColorInt toolbarColor: Int) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(toolbarColor)
                .build()
        )
        .build()

    customTabsIntent.launchUrl(context, url)
}
