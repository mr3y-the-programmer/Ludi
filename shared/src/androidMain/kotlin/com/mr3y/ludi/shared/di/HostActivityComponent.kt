package com.mr3y.ludi.shared.di

import android.app.Activity
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class HostActivityComponent(
    @get:Provides val activity: Activity,
    @Component val parent: AndroidApplicationComponent
) : ScreenModelsComponent {

}