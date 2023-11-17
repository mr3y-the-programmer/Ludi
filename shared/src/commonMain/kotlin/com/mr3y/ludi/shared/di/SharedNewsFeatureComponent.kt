package com.mr3y.ludi.shared.di

import cafe.adriel.voyager.core.model.ScreenModel
import com.mr3y.ludi.shared.di.annotations.NewsFeatureScope
import com.mr3y.ludi.shared.ui.presenter.NewsViewModel
import me.tatarka.inject.annotations.Provides

interface SharedNewsFeatureComponent {

    val bind: ScreenModel

    @Provides
    @NewsFeatureScope
    fun NewsViewModel.provideNewsViewModelInstance(): ScreenModel = this
}
