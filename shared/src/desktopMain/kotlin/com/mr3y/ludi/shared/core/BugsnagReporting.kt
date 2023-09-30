package com.mr3y.ludi.shared.core

import com.bugsnag.Bugsnag
import me.tatarka.inject.annotations.Inject

@Inject
class BugsnagReporting(
    private val bugsnagClient: Bugsnag
) : CrashReporting {

    override fun recordException(throwable: Throwable, logMessage: String?) {
        bugsnagClient.notify(throwable)
    }
}