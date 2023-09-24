package com.mr3y.ludi

import android.app.Activity
import android.content.Context
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mr3y.ludi.di.ApplicationComponent
import com.mr3y.ludi.di.ScreenModelsComponent
import com.mr3y.ludi.ui.presenter.model.Theme
import com.mr3y.ludi.ui.screens.home.HomeScreen
import com.mr3y.ludi.ui.screens.onboarding.OnboardingScreen
import com.mr3y.ludi.ui.theme.LudiTheme
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

class MainActivity : ComponentActivity() {

    internal lateinit var component: MainActivityComponent

    private val viewModel: MainActivityViewModel by viewModels {
        viewModelFactory {
            addInitializer(MainActivityViewModel::class) { component.viewModel() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        component = MainActivityComponent::class.create(this)

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
                        Navigator(screen = OnboardingScreen)
                    } else {
                        HomeScreen(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Component
abstract class MainActivityComponent(
    @get:Provides val activity: Activity,
    @Component val parent: ApplicationComponent = ApplicationComponent.from(activity)
) : ScreenModelsComponent {
    abstract val viewModel: () -> MainActivityViewModel
}

val Context.activityComponent
    get() = (this as MainActivity).component
