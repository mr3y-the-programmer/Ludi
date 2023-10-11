package com.mr3y.ludi

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mr3y.ludi.shared.App
import com.mr3y.ludi.shared.di.AndroidApplicationComponent
import com.mr3y.ludi.shared.di.HostActivityComponent
import com.mr3y.ludi.shared.di.HostActivityComponentOwner
import com.mr3y.ludi.shared.di.create
import com.mr3y.ludi.shared.ui.presenter.model.Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), HostActivityComponentOwner {

    override val hostActivityComponent: HostActivityComponent by lazy {
        HostActivityComponent::class.create(this, AndroidApplicationComponent.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Initialize the component.
        hostActivityComponent

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val appState = hostActivityComponent.parent.appState
        lifecycleScope.launch {
            appState.preferences.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect { userPreferences ->
                splashScreen.setKeepOnScreenCondition { userPreferences == null }
            }
        }
        setContent {
            val userPreferences by appState.preferences.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.CREATED)
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
                App(
                    isDarkTheme = when (userPreferences!!.theme) {
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
                    useDynamicColor = userPreferences!!.useDynamicColor,
                    showOnboardingScreen = userPreferences!!.showOnBoardingScreen
                )
            }
        }
    }
}

private fun AndroidApplicationComponent.Companion.from(activity: Activity): AndroidApplicationComponent {
    return (activity.applicationContext as LudiApplication).component
}
