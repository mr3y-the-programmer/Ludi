package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.shared.core.network.datasources.internal.DefaultRSSFeedDataSource
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface RSSFeedDataSourcesComponent {

    @Provides
    @Singleton
    fun DefaultRSSFeedDataSource.bind(): RSSFeedDataSource = this
}
