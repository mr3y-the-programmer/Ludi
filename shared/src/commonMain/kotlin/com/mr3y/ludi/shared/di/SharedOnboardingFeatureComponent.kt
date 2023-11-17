package com.mr3y.ludi.shared.di

import cafe.adriel.voyager.core.model.ScreenModel
import com.mr3y.ludi.shared.di.annotations.OnboardingFeatureScope
import com.mr3y.ludi.shared.ui.presenter.OnBoardingViewModel
import me.tatarka.inject.annotations.Provides

interface SharedOnboardingFeatureComponent {

    val bind: ScreenModel

    @Provides
    @OnboardingFeatureScope
    fun OnBoardingViewModel.provideOnboardingViewModelInstance(): ScreenModel = this
}
