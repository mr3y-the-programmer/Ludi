package com.mr3y.ludi.desktop

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mr3y.ludi.shared.App
import com.mr3y.ludi.shared.di.DesktopApplicationComponent
import com.mr3y.ludi.shared.di.HostWindowComponent
import com.mr3y.ludi.shared.di.LocalHostWindowComponent
import com.mr3y.ludi.shared.di.create
import com.mr3y.ludi.shared.ui.presenter.model.Theme

fun main() {
    val appComponent = DesktopApplicationComponent::class.create()
    application {
        val preferences by appComponent.appState.preferences.collectAsState()
        Window(
            title = "Ludi",
            onCloseRequest = ::exitApplication,
            icon = painterResource("icon_light.xml")
        ) {
            val windowComponent = HostWindowComponent::class.create(window, appComponent)
            CompositionLocalProvider(
                LocalHostWindowComponent provides windowComponent
            ) {
                if (preferences != null) {
                    App(
                        isDarkTheme = when (preferences!!.theme) {
                            Theme.Light -> false
                            Theme.Dark -> true
                            Theme.SystemDefault -> isSystemInDarkTheme()
                        },
                        useDynamicColor = preferences!!.useDynamicColor,
                        showOnboardingScreen = preferences!!.showOnBoardingScreen
                    )
                }
            }
        }
    }
}
