package com.mr3y.ludi.di

import com.mr3y.ludi.core.repository.DealsRepository
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.NewsRepository
import com.mr3y.ludi.core.repository.internal.DefaultDealsRepository
import com.mr3y.ludi.core.repository.internal.DefaultGamesRepository
import com.mr3y.ludi.core.repository.internal.DefaultNewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    @Singleton
    abstract fun DefaultNewsRepository.bindNewsRepository(): NewsRepository

    @Binds
    @Singleton
    abstract fun DefaultGamesRepository.bindGamesRepository(): GamesRepository

    @Binds
    @Singleton
    abstract fun DefaultDealsRepository.bindDealsRepository(): DealsRepository
}
