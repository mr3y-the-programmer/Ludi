package com.mr3y.ludi.di

import com.mr3y.ludi.core.network.datasources.internal.MMOGamesDataSource
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
}