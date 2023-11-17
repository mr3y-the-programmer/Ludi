package com.mr3y.ludi.shared.di

import cafe.adriel.voyager.core.model.ScreenModel
import com.mr3y.ludi.shared.di.annotations.DealsFeatureScope
import com.mr3y.ludi.shared.ui.presenter.DealsViewModel
import me.tatarka.inject.annotations.Provides

interface SharedDealsFeatureComponent {

    val bind: ScreenModel

    @Provides
    @DealsFeatureScope
    fun DealsViewModel.provideDealsViewModelInstance(): ScreenModel = this
}
