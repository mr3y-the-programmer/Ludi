package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.Singleton
import com.mr3y.ludi.shared.ui.presenter.usecases.GetSearchQueryBasedGamesUseCase
import com.mr3y.ludi.shared.ui.presenter.usecases.GetSearchQueryBasedGamesUseCaseImpl
import com.mr3y.ludi.shared.ui.presenter.usecases.GetSuggestedGamesUseCase
import com.mr3y.ludi.shared.ui.presenter.usecases.GetSuggestedGamesUseCaseImpl
import me.tatarka.inject.annotations.Provides

interface UseCasesComponent {

    @Provides
    @Singleton
    fun GetSuggestedGamesUseCaseImpl.bind(): GetSuggestedGamesUseCase = this

    @Provides
    @Singleton
    fun GetSearchQueryBasedGamesUseCaseImpl.bind(): GetSearchQueryBasedGamesUseCase = this
}
