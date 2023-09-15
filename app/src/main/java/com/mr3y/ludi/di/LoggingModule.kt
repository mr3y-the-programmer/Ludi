package com.mr3y.ludi.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.mr3y.ludi.core.CrashReporting
import com.mr3y.ludi.core.internal.CrashlyticsReporting
import com.mr3y.ludi.core.internal.KermitLogger
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

    @Singleton
    @Provides
    fun provideKermitLoggerInstance(kermitLogger: Logger): com.mr3y.ludi.core.Logger {
        return KermitLogger(delegatingLogger = kermitLogger)
    }

    @Singleton
    @Provides
    fun provideCrashlyticsInstance(): FirebaseCrashlytics {
        return Firebase.crashlytics
    }

    @Singleton
    @Provides
    fun provideCrashReportingInstance(crashlytics: FirebaseCrashlytics): CrashReporting {
        return CrashlyticsReporting(crashlytics)
    }
}
