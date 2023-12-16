package com.mr3y.ludi.shared.di

import com.mr3y.ludi.shared.di.annotations.Singleton
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Provides

actual interface DatabaseDispatcherComponent {

    @Singleton
    @Provides
    fun provideMainDispatcher(): DatabaseDispatcher {
        return DatabaseDispatcher(Dispatchers.Main)
    }
}
