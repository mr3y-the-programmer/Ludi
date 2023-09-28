package com.mr3y.ludi.di

import com.mr3y.ludi.di.annotations.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides

@JvmInline
value class ApplicationScope(val value: CoroutineScope)

interface CoroutinesComponent {

    @Singleton
    @Provides
    fun providesApplicationCoroutineScope(): ApplicationScope {
        return ApplicationScope(CoroutineScope(SupervisorJob() + Dispatchers.IO))
    }
}
