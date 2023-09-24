package com.mr3y.ludi.di

import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.network.datasources.internal.DefaultRSSFeedDataSource
import me.tatarka.inject.annotations.Provides

interface RSSFeedDataSourcesModule {

    @Provides
    @Singleton
    fun DefaultRSSFeedDataSource.bind(): RSSFeedDataSource = this
}
