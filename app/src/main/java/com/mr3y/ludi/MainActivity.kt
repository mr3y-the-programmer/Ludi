package com.mr3y.ludi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
        lifecycleScope.launch {
            viewModel.preferences.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect { userPreferences ->
                splashScreen.setKeepOnScreenCondition { userPreferences == null }
            }
        }
        setContent {
            val userPreferences by viewModel.preferences.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.CREATED)
            if (userPreferences != null) {
                LudiTheme(
                    darkTheme = when (userPreferences!!.theme) {
                        Theme.Light -> false
                        Theme.Dark -> true
                        Theme.SystemDefault -> isSystemInDarkTheme()
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
