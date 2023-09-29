package com.mr3y.ludi.shared.core

interface CrashReporting {

    fun recordException(throwable: Throwable, logMessage: String?)
}
