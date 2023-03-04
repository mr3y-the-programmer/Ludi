package com.mr3y.ludi.di

import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.network.datasources.internal.GameSpotDataSource
import com.mr3y.ludi.core.network.datasources.internal.GiantBombDataSource
import com.mr3y.ludi.core.network.datasources.internal.IGNDataSource
import com.mr3y.ludi.core.network.model.RSSFeedArticle
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RSSFeedDataSourcesModule {

    @Binds
    @Singleton
    @IntoSet
    abstract fun bindGiantBombDataSource(dataSource: GiantBombDataSource): RSSFeedDataSource<RSSFeedArticle>

    @Binds
    @Singleton
    @IntoSet
    abstract fun bindGameSpotDataSource(dataSource: GameSpotDataSource): RSSFeedDataSource<RSSFeedArticle>

    @Binds
    @Singleton
    @IntoSet
    abstract fun bindIGNDataSource(dataSource: IGNDataSource): RSSFeedDataSource<RSSFeedArticle>
}