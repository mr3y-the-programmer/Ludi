package com.mr3y.ludi.shared.di

import android.app.Activity
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.EditPreferencesViewModel
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class HostActivityComponent(
    @get:Provides val activity: Activity,
    @Component val parent: AndroidApplicationComponent
) {
    abstract val editPreferencesViewModelFactory: (PreferencesType) -> EditPreferencesViewModel
}
