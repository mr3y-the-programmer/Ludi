package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.core.repository.DealsRepository
import com.mr3y.ludi.shared.core.repository.GamesRepository
import com.mr3y.ludi.shared.core.repository.NewsRepository
import com.mr3y.ludi.shared.core.repository.internal.DefaultDealsRepository
import com.mr3y.ludi.shared.core.repository.internal.DefaultGamesRepository
import com.mr3y.ludi.shared.core.repository.internal.DefaultNewsRepository
import com.mr3y.ludi.shared.di.annotations.Singleton
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
