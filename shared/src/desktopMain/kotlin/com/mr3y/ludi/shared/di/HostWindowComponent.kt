package com.mr3y.ludi.shared.di

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.awt.ComposeWindow
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.EditPreferencesViewModel
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class HostWindowComponent(
    @get:Provides val window: ComposeWindow,
    @Component val parent: DesktopApplicationComponent
) {
    abstract val editPreferencesViewModelFactory: (PreferencesType) -> EditPreferencesViewModel
}

val LocalHostWindowComponent = staticCompositionLocalOf<HostWindowComponent> { error("HostWindowComponent isn't provided!") }
