package com.mr3y.ludi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mr3y.ludi.ui.screens.main.MainScreen
import com.mr3y.ludi.ui.theme.LudiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudiTheme {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
