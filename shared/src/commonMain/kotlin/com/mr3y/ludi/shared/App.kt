package com.mr3y.ludi.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.mr3y.ludi.shared.ui.resources.ProvideCompositionLocalValues
import com.mr3y.ludi.shared.ui.screens.home.HomeScreen
import com.mr3y.ludi.shared.ui.screens.onboarding.OnboardingScreen
import com.mr3y.ludi.shared.ui.theme.LudiTheme

@Composable
fun App(
    isDarkTheme: Boolean,
    useDynamicColor: Boolean,
    showOnboardingScreen: Boolean,
    modifier: Modifier = Modifier
) {
    ProvideCompositionLocalValues {
        LudiTheme(
            darkTheme = isDarkTheme,
            dynamicColor = useDynamicColor
        ) {
            if (showOnboardingScreen) {
                Navigator(screen = OnboardingScreen)
            } else {
                HomeScreen(modifier = modifier.fillMaxSize())
            }
        }
    }
}