package com.mr3y.ludi.di

import com.mr3y.ludi.core.network.datasources.internal.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RESTfulDataSourcesModule {

    @Singleton
    @Provides
    fun provideMMOGamesDataSourceInstance(retrofit: Retrofit): MMOGamesDataSource {
        return retrofit.create()
    }

    @Singleton
    @Provides
    fun provideFreeToGameDataSourceInstance(retrofit: Retrofit): FreeToGameDataSource {
        return retrofit.create()
    }

    @Singleton
    @Provides
    fun provideRAWGDataSourceInstance(retrofit: Retrofit): RAWGDataSource {
        return retrofit.create()
    }

    @Singleton
    @Provides
    fun provideCheapSharkDataSourceInstance(retrofit: Retrofit): CheapSharkDataSource {
        return retrofit.create()
    }

    @Singleton
    @Provides
    fun provideGamerPowerDataSourceInstance(retrofit: Retrofit): GamerPowerDataSource {
        return retrofit.create()
    }
}