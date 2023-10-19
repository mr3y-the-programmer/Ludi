package com.mr3y.ludi.shared.di

import cafe.adriel.voyager.core.model.ScreenModel
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.DealsViewModel
import com.mr3y.ludi.shared.ui.presenter.DefaultDiscoverPagingFactory
import com.mr3y.ludi.shared.ui.presenter.DiscoverPagingFactory
import com.mr3y.ludi.shared.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.shared.ui.presenter.EditPreferencesViewModel
import com.mr3y.ludi.shared.ui.presenter.NewsViewModel
import com.mr3y.ludi.shared.ui.presenter.OnBoardingViewModel
import com.mr3y.ludi.shared.ui.presenter.SettingsViewModel
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

    @Provides
    fun DefaultDiscoverPagingFactory.bind(): DiscoverPagingFactory = this

    val editPreferencesViewModelFactory: (PreferencesType) -> EditPreferencesViewModel
}
