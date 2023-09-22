package com.mr3y.ludi.core.internal

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mr3y.ludi.core.CrashReporting
import javax.inject.Inject

class CrashlyticsReporting @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : CrashReporting {

    override fun recordException(throwable: Throwable, logMessage: String?) {
        with(crashlytics) {
            if (logMessage != null) {
                log(logMessage)
            }
            recordException(throwable)
        }
    }
}
