package com.mr3y.ludi.di

import com.mr3y.ludi.core.network.datasources.internal.CheapSharkDataSource
import com.mr3y.ludi.core.network.datasources.internal.CheapSharkDataSourceImpl
import com.mr3y.ludi.core.network.datasources.internal.GamerPowerDataSource
import com.mr3y.ludi.core.network.datasources.internal.GamerPowerDataSourceImpl
import com.mr3y.ludi.core.network.datasources.internal.RAWGDataSource
import com.mr3y.ludi.core.network.datasources.internal.RAWGDataSourceImpl
import io.ktor.client.HttpClient
import me.tatarka.inject.annotations.Provides

interface RESTfulDataSourcesModule {

    @Provides
    @Singleton
    fun provideRAWGDataSourceInstance(client: HttpClient): RAWGDataSource {
        return RAWGDataSourceImpl(client)
    }

    @Provides
    @Singleton
    fun provideCheapSharkDataSourceInstance(client: HttpClient): CheapSharkDataSource {
        return CheapSharkDataSourceImpl(client)
    }

    @Provides
    @Singleton
    fun provideGamerPowerDataSourceInstance(client: HttpClient): GamerPowerDataSource {
        return GamerPowerDataSourceImpl(client)
    }
}
