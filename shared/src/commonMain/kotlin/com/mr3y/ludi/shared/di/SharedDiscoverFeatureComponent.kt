package com.mr3y.ludi.shared.di

import cafe.adriel.voyager.core.model.ScreenModel
import com.mr3y.ludi.shared.di.annotations.DiscoverFeatureScope
import com.mr3y.ludi.shared.ui.presenter.DefaultDiscoverPagingFactory
import com.mr3y.ludi.shared.ui.presenter.DiscoverPagingFactory
import com.mr3y.ludi.shared.ui.presenter.DiscoverViewModel
import me.tatarka.inject.annotations.Provides

interface SharedDiscoverFeatureComponent {

    val bind: ScreenModel

    @Provides
    @DiscoverFeatureScope
    fun DiscoverViewModel.provideDiscoverViewModelInstance(): ScreenModel = this

    @Provides
    @DiscoverFeatureScope
    fun DefaultDiscoverPagingFactory.bind(): DiscoverPagingFactory = this
}
