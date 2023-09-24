package com.mr3y.ludi.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides

@JvmInline
value class ApplicationScope(val value: CoroutineScope)

interface CoroutinesModule {

    @Singleton
    @Provides
    fun providesApplicationCoroutineScope(): ApplicationScope {
        return ApplicationScope(CoroutineScope(SupervisorJob() + Dispatchers.IO))
    }
}
