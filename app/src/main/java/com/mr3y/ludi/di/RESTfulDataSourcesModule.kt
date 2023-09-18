package com.mr3y.ludi.di

import com.mr3y.ludi.core.network.datasources.internal.CheapSharkDataSource
import com.mr3y.ludi.core.network.datasources.internal.CheapSharkDataSourceImpl
import com.mr3y.ludi.core.network.datasources.internal.GamerPowerDataSource
import com.mr3y.ludi.core.network.datasources.internal.GamerPowerDataSourceImpl
import com.mr3y.ludi.core.network.datasources.internal.MMOGamesDataSource
import com.mr3y.ludi.core.network.datasources.internal.MMOGamesDataSourceImpl
import com.mr3y.ludi.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.core.network.datasources.internal.RAWGDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RESTfulDataSourcesModule {

    @Singleton
    @Provides
    fun provideMMOGamesDataSourceInstance(client: HttpClient): MMOGamesDataSource {
        return MMOGamesDataSourceImpl(client)
    }

    @Singleton
    @Provides
    fun provideRAWGDataSourceInstance(client: HttpClient): RAWGDataSource {
        return RAWGDataSourceImpl(client)
    }

    @Singleton
    @Provides
    fun provideCheapSharkDataSourceInstance(client: HttpClient): CheapSharkDataSource {
        return CheapSharkDataSourceImpl(client)
    }

    @Singleton
    @Provides
    fun provideGamerPowerDataSourceInstance(client: HttpClient): GamerPowerDataSource {
        return GamerPowerDataSourceImpl(client)
    }
}
