package com.mr3y.ludi.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import com.mr3y.ludi.shared.ui.adaptive.LocalWindowSizeClass
import com.mr3y.ludi.shared.ui.screens.home.HomeScreen
import com.mr3y.ludi.shared.ui.screens.onboarding.OnboardingScreen
import com.mr3y.ludi.shared.ui.theme.LudiTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalCoilApi::class)
@Composable
fun App(
    isDarkTheme: Boolean,
    useDynamicColor: Boolean,
    showOnboardingScreen: Boolean,
    modifier: Modifier = Modifier
) {
    LudiTheme(
        darkTheme = isDarkTheme,
        dynamicColor = useDynamicColor
    ) {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context).build()
        }
        CompositionLocalProvider(
            LocalWindowSizeClass provides calculateWindowSizeClass()
        ) {
            if (showOnboardingScreen) {
                Navigator(screen = OnboardingScreen)
            } else {
                HomeScreen(modifier = modifier.fillMaxSize())
            }
        }
    }
}
