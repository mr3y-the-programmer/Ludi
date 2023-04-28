package com.mr3y.ludi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.ui.presenter.model.Theme
import com.mr3y.ludi.ui.screens.main.MainScreen
import com.mr3y.ludi.ui.theme.LudiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userPreferences by viewModel.preferences.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.CREATED)
            LudiTheme(
                darkTheme = when(userPreferences.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.SystemDefault -> isSystemInDarkTheme()
                },
                dynamicColor = userPreferences.useDynamicColor
            ) {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
