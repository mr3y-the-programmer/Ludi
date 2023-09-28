package com.mr3y.ludi.di

import cafe.adriel.voyager.core.model.ScreenModel
import com.mr3y.ludi.ui.navigation.PreferencesType
import com.mr3y.ludi.ui.presenter.DealsViewModel
import com.mr3y.ludi.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.ui.presenter.EditPreferencesViewModel
import com.mr3y.ludi.ui.presenter.NewsViewModel
import com.mr3y.ludi.ui.presenter.OnBoardingViewModel
import com.mr3y.ludi.ui.presenter.SettingsViewModel
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import kotlin.reflect.KClass

interface ScreenModelsComponent {
    val screenModels: Map<KClass<out ScreenModel>, () -> ScreenModel>

    @Provides
    @IntoMap
    fun OnBoardingViewModel.bind(): Pair<KClass<out ScreenModel>, () -> ScreenModel> = this::class to { this }

    @Provides
    @IntoMap
    fun DiscoverViewModel.bind(): Pair<KClass<out ScreenModel>, () -> ScreenModel> = this::class to { this }

    @Provides
    @IntoMap
    fun NewsViewModel.bind(): Pair<KClass<out ScreenModel>, () -> ScreenModel> = this::class to { this }

    @Provides
    @IntoMap
    fun DealsViewModel.bind(): Pair<KClass<out ScreenModel>, () -> ScreenModel> = this::class to { this }

    @Provides
    @IntoMap
    fun SettingsViewModel.bind(): Pair<KClass<out ScreenModel>, () -> ScreenModel> = this::class to { this }

    val editPreferencesViewModelFactory: (PreferencesType) -> EditPreferencesViewModel
}
