package com.mr3y.ludi.di

import com.mr3y.ludi.core.repository.DealsRepository
import com.mr3y.ludi.core.repository.GamesRepository
import com.mr3y.ludi.core.repository.NewsRepository
import com.mr3y.ludi.core.repository.internal.DefaultDealsRepository
import com.mr3y.ludi.core.repository.internal.DefaultGamesRepository
import com.mr3y.ludi.core.repository.internal.DefaultNewsRepository
import com.mr3y.ludi.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface RepositoriesComponent {

    @Provides
    @Singleton
    fun DefaultNewsRepository.bind(): NewsRepository = this

    @Provides
    @Singleton
    fun DefaultGamesRepository.bind(): GamesRepository = this

    @Provides
    @Singleton
    fun DefaultDealsRepository.bind(): DealsRepository = this
}
