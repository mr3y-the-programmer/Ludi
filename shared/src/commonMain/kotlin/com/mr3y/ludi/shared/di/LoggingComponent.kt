package com.mr3y.ludi.shared.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.mr3y.ludi.shared.core.internal.KermitLogger
import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Provides

interface LoggingComponent {

    @Singleton
    @Provides
    fun provideLoggerInstance(): Logger {
        return Logger(config = loggerConfigInit(platformLogWriter()))
    }

    @Singleton
    @Provides
    fun provideKermitLoggerInstance(kermitLogger: Logger): com.mr3y.ludi.shared.core.Logger {
        return KermitLogger(delegatingLogger = kermitLogger)
    }
}
