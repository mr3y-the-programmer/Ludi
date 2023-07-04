package com.mr3y.ludi.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

    @Singleton
    @Provides
    fun provideLoggerInstance(): Logger {
        return Logger(config = loggerConfigInit(platformLogWriter()))
    }
}
