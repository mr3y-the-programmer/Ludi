package com.mr3y.ludi.shared.di

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.awt.ComposeWindow
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class HostWindowComponent(
    @get:Provides val window: ComposeWindow,
    @Component val parent: DesktopApplicationComponent
) : ScreenModelsComponent

val LocalHostWindowComponent = staticCompositionLocalOf<HostWindowComponent> { error("HostWindowComponent isn't provided!") }
