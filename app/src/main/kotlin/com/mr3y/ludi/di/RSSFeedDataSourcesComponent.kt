package com.mr3y.ludi.di

import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.network.datasources.internal.DefaultRSSFeedDataSource
import com.mr3y.ludi.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface RSSFeedDataSourcesComponent {

    @Provides
    @Singleton
    fun DefaultRSSFeedDataSource.bind(): RSSFeedDataSource = this
}
