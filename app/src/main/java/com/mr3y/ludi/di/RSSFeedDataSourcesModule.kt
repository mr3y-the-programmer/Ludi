package com.mr3y.ludi.di

import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.network.datasources.internal.GameSpotDataSource
import com.mr3y.ludi.core.network.datasources.internal.GiantBombDataSource
import com.mr3y.ludi.core.network.datasources.internal.IGNDataSource
import com.mr3y.ludi.core.network.datasources.internal.TechRadarGamingDataSource
import com.mr3y.ludi.core.network.model.RSSFeedArticle
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RSSFeedDataSourcesModule {

    @Binds
    @Singleton
    @IntoMap
    @SourceKey(Source.GiantBomb)
    abstract fun bindGiantBombDataSource(dataSource: GiantBombDataSource): RSSFeedDataSource<RSSFeedArticle>

    @Binds
    @Singleton
    @IntoMap
    @SourceKey(Source.GameSpot)
    abstract fun bindGameSpotDataSource(dataSource: GameSpotDataSource): RSSFeedDataSource<RSSFeedArticle>

    @Binds
    @Singleton
    @IntoMap
    @SourceKey(Source.IGN)
    abstract fun bindIGNDataSource(dataSource: IGNDataSource): RSSFeedDataSource<RSSFeedArticle>

    @Binds
    @Singleton
    @IntoMap
    @SourceKey(Source.TechRadar)
    abstract fun bindTechRadarDataSource(dataSource: TechRadarGamingDataSource): RSSFeedDataSource<RSSFeedArticle>
}
