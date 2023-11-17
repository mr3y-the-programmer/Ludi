package com.mr3y.ludi.shared.di

import cafe.adriel.voyager.core.model.ScreenModel
import com.mr3y.ludi.shared.di.annotations.SettingsFeatureScope
import com.mr3y.ludi.shared.ui.presenter.SettingsViewModel
import me.tatarka.inject.annotations.Provides

interface SharedSettingsFeatureComponent {

    val bind: ScreenModel

    @Provides
    @SettingsFeatureScope
    fun SettingsViewModel.provideSettingsViewModelInstance(): ScreenModel = this
}
