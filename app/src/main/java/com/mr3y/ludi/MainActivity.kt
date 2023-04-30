package com.mr3y.ludi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mr3y.ludi.ui.presenter.model.Theme
import com.mr3y.ludi.ui.screens.main.MainScreen
import com.mr3y.ludi.ui.screens.onboarding.OnboardingScreen
import com.mr3y.ludi.ui.theme.LudiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        lifecycleScope.launch {
            viewModel.preferences.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect { userPreferences ->
                splashScreen.setKeepOnScreenCondition { userPreferences == null }
            }
        }
        setContent {
            val userPreferences by viewModel.preferences.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.CREATED)
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            DisposableEffect(systemUiController, useDarkIcons) {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )

                onDispose {}
            }
            if (userPreferences != null) {
                LudiTheme(
                    darkTheme = when (userPreferences!!.theme) {
                        Theme.Light -> {
                            systemUiController.setSystemBarsColor(Color.Transparent, true)
                            false
                        }
                        Theme.Dark -> {
                            systemUiController.setSystemBarsColor(Color.Transparent, false)
                            true
                        }
                        Theme.SystemDefault -> {
                            val isDarkTheme = isSystemInDarkTheme()
                            systemUiController.setSystemBarsColor(Color.Transparent, !isDarkTheme)
                            isDarkTheme
                        }
                    },
                    dynamicColor = userPreferences!!.useDynamicColor
                ) {
                    if (userPreferences!!.showOnBoardingScreen) {
                        OnboardingScreen(modifier = Modifier.fillMaxSize())
                    } else {
                        MainScreen(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
