package com.mr3y.ludi.di

import com.mr3y.ludi.ui.presenter.usecases.GetSearchQueryBasedGamesUseCase
import com.mr3y.ludi.ui.presenter.usecases.GetSearchQueryBasedGamesUseCaseImpl
import com.mr3y.ludi.ui.presenter.usecases.GetSuggestedGamesUseCase
import com.mr3y.ludi.ui.presenter.usecases.GetSuggestedGamesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {

    @Binds
    @Singleton
    abstract fun GetSuggestedGamesUseCaseImpl.bindSuggestedGamesUseCase(): GetSuggestedGamesUseCase

    @Binds
    @Singleton
    abstract fun GetSearchQueryBasedGamesUseCaseImpl.bindSearchQueryBasedGamesUseCase(): GetSearchQueryBasedGamesUseCase
}
