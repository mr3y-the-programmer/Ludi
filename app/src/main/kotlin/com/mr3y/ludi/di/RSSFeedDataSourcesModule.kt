package com.mr3y.ludi.di

import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.network.datasources.internal.DefaultRSSFeedDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RSSFeedDataSourcesModule {

    @Binds
    @Singleton
    abstract fun bindRSSFeedDataSource(impl: DefaultRSSFeedDataSource): RSSFeedDataSource
}
