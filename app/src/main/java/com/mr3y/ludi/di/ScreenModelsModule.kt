package com.mr3y.ludi.di

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.hilt.ScreenModelFactory
import cafe.adriel.voyager.hilt.ScreenModelFactoryKey
import cafe.adriel.voyager.hilt.ScreenModelKey
import com.mr3y.ludi.ui.presenter.DealsViewModel
import com.mr3y.ludi.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.ui.presenter.EditPreferencesViewModel
import com.mr3y.ludi.ui.presenter.NewsViewModel
import com.mr3y.ludi.ui.presenter.OnBoardingViewModel
import com.mr3y.ludi.ui.presenter.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ScreenModelsModule {

    @Binds
    @IntoMap
    @ScreenModelKey(OnBoardingViewModel::class)
    abstract fun bindOnboardingViewModel(impl: OnBoardingViewModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(DiscoverViewModel::class)
    abstract fun bindDiscoverViewModel(impl: DiscoverViewModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(impl: NewsViewModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(DealsViewModel::class)
    abstract fun bindDealsViewModel(impl: DealsViewModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(impl: SettingsViewModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelFactoryKey(EditPreferencesViewModel.Factory::class)
    abstract fun bindEditPreferencesViewModelFactory(impl: EditPreferencesViewModel.Factory): ScreenModelFactory
}
