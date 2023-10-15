package com.mr3y.ludi.shared.core

import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.tatarka.inject.annotations.Inject

@Inject
class CrashlyticsReporting(
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
