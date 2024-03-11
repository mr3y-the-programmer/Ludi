package com.mr3y.ludi.shared.di

import cafe.adriel.voyager.core.model.ScreenModel
import com.mr3y.ludi.shared.core.repository.NewsRepository
import com.mr3y.ludi.shared.di.annotations.NewsFeatureScope
import com.mr3y.ludi.shared.ui.datastore.FollowedNewsDataSourcesDataStore
import com.mr3y.ludi.shared.ui.presenter.NewsFeedThrottler
import com.mr3y.ludi.shared.ui.presenter.NewsViewModel
import me.tatarka.inject.annotations.Provides

interface SharedNewsFeatureComponent {

    val bind: ScreenModel

    @Provides
    @NewsFeatureScope
    fun provideNewsViewModelInstance(
        newsRepository: NewsRepository,
        followedNewsDataSourcesDataStore: FollowedNewsDataSourcesDataStore,
        throttler: NewsFeedThrottler
    ): ScreenModel {
        return NewsViewModel(newsRepository, followedNewsDataSourcesDataStore.value, throttler)
    }
}
