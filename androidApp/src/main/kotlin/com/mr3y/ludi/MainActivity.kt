package com.mr3y.ludi

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mr3y.ludi.shared.App
import com.mr3y.ludi.shared.UserPreferences
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

        val appState = hostActivityComponent.parent.appState
        lifecycleScope.launch {
            appState.preferences.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect { userPreferences ->
                splashScreen.setKeepOnScreenCondition { userPreferences == null }
            }
        }

        goEdgeToEdge()

        setContent {
            val userPreferences by appState.preferences.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.CREATED)
            val useDarkIcons = shouldUseDarkIcons(userPreferences)

            DisposableEffect(useDarkIcons) {
                goEdgeToEdge(isDarkTheme = { !useDarkIcons })

                onDispose {}
            }
            if (userPreferences != null) {
                App(
                    isDarkTheme = when (userPreferences!!.theme) {
                        Theme.Light -> {
                            goEdgeToEdge(isDarkTheme = { false })
                            false
                        }
                        Theme.Dark -> {
                            goEdgeToEdge(isDarkTheme = { true })
                            true
                        }
                        Theme.SystemDefault -> {
                            val isDarkTheme = isSystemInDarkTheme()
                            goEdgeToEdge(isDarkTheme = { isDarkTheme })
                            isDarkTheme
                        }
                    },
                    useDynamicColor = userPreferences!!.useDynamicColor,
                    showOnboardingScreen = userPreferences!!.showOnBoardingScreen
                )
            }
        }
    }

    private fun goEdgeToEdge(
        isDarkTheme: (Resources) -> Boolean = { resources ->
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        }
    ) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
                detectDarkMode = isDarkTheme
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b),
                detectDarkMode = isDarkTheme
            )
        )
    }

    @Composable
    private fun shouldUseDarkIcons(prefs: UserPreferences?): Boolean {
        if (prefs == null) {
            return !isSystemInDarkTheme()
        }

        return when(prefs.theme) {
            Theme.Light -> true
            Theme.Dark -> false
            Theme.SystemDefault -> !isSystemInDarkTheme()
        }
    }
}

private fun AndroidApplicationComponent.Companion.from(activity: Activity): AndroidApplicationComponent {
    return (activity.applicationContext as LudiApplication).component
}
